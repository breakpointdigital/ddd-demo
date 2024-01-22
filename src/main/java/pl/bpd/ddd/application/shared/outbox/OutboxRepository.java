package pl.bpd.ddd.application.shared.outbox;

import java.util.Collection;
import java.util.List;

public interface OutboxRepository {
    List<OutboxItem> findAllToProcess();

    void saveAll(Collection<OutboxItem> outboxItems);

    void markAsProcessed(long outboxItemId);
}
