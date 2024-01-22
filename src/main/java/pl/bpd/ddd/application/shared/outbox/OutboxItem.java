package pl.bpd.ddd.application.shared.outbox;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
public class OutboxItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Class<?> type;
    @Column(columnDefinition = "text", nullable = false)
    private String data;
    @Column(nullable = false)
    private Instant occurredAt;
    private Instant processedAt;

    public OutboxItem(Class<?> type, String data, Instant now) {
        this.type = type;
        this.data = data;
        this.occurredAt = now;
    }
}
