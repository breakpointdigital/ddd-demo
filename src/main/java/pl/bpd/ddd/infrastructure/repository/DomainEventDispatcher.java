package pl.bpd.ddd.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.bpd.ddd.application.shared.outbox.OutboxService;
import pl.bpd.ddd.domain.shared.DomainEntity;


@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventDispatcher {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OutboxService outboxService;

    @Transactional(propagation = Propagation.MANDATORY)
    public void publishEntity(DomainEntity entity) {
        log.debug("Dispatching {} events from {} entity", entity.getDomainEvents().size(), entity.getClass());

        // publish domain events in the same transaction
        entity.getDomainEvents().forEach(applicationEventPublisher::publishEvent);
        // add domain events to outbox to publish them asynchronously
        outboxService.addAll(entity.getDomainEvents());
        entity.clearDomainEvents();
    }
}
