package pl.bpd.ddd.application.shared.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.bpd.ddd.application.shared.events.DomainEventNotification;
import pl.bpd.ddd.domain.shared.DomainEvent;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void addAll(List<DomainEvent> events) {
        var outboxItems = events.stream().map(this::createItem).toList();
        outboxRepository.saveAll(outboxItems);
    }

    private OutboxItem createItem(Object event) {
        try {
            String jsonData = objectMapper.writeValueAsString(event);
            return new OutboxItem(event.getClass(), jsonData, Instant.now());
        } catch (JsonProcessingException e) {
            log.error("Cannot serialize event {} for outbox", event, e);
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "${app.outbox.cron}")
    public void processPendingOutboxItems() {
        var items = outboxRepository.findAllToProcess();
        log.info("Processing {} items from outbox", items.size());
        for (OutboxItem item : items) {
            processItem(item);
        }
        log.info("Processing outbox items done");
    }

    // not using transaction, as a long-running process is not transactional and, what's more important,
    // we don't want to lock a database connection for a longer time
    private void processItem(OutboxItem item) {
        try {
            log.debug("Processing outbox item id: {} of type {}", item.getId(), item.getType());
            var event = objectMapper.readValue(item.getData(), item.getType());
            applicationEventPublisher.publishEvent(new DomainEventNotification<>(event));
            outboxRepository.markAsProcessed(item.getId());
            log.debug("Outbox item id: {} processed successfully", item.getId());
        } catch (Exception e) {
            log.error("Cannot handle outbox item id: {}", item.getId(), e);
        }
    }
}
