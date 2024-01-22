package pl.bpd.ddd.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.bpd.ddd.application.shared.outbox.OutboxItem;

import java.time.Instant;
import java.util.List;

public interface OutboxQueries extends JpaRepository<OutboxItem, Long> {
    List<OutboxItem> findByProcessedAtIsNull();

    @Modifying
    @Query("update OutboxItem set processedAt = :now where id = :id")
    void markAsProcessed(@Param("id") long id, @Param("now") Instant now);
}
