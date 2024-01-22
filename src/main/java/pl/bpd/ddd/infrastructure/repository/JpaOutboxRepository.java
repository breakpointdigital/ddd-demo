package pl.bpd.ddd.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.bpd.ddd.application.shared.outbox.OutboxItem;
import pl.bpd.ddd.application.shared.outbox.OutboxRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class JpaOutboxRepository implements OutboxRepository {
    private final OutboxQueries outboxQueries;

    @Override
    public List<OutboxItem> findAllToProcess() {
        return outboxQueries.findByProcessedAtIsNull();
    }

    @Override
    public void saveAll(Collection<OutboxItem> outboxItems) {
        outboxQueries.saveAll(outboxItems);
    }

    @Override
    public void markAsProcessed(long outboxItemId) {
        outboxQueries.markAsProcessed(outboxItemId, Instant.now());
    }
}
