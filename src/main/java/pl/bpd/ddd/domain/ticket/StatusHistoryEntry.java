package pl.bpd.ddd.domain.ticket;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pl.bpd.ddd.domain.member.Member;

import java.time.Instant;

/**
 * Value object instead of entity, as we don't need to modify it
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class StatusHistoryEntry {
    @Enumerated(EnumType.STRING)
    private TicketStatus previousStatus;
    @ManyToOne
    @JoinColumn(name = "editor_id")
    private Member editor;
    private Instant changeDate;

    public TicketStatus previousStatus() {
        return previousStatus;
    }

    public Member editor() {
        return editor;
    }

    public Instant changeDate() {
        return changeDate;
    }
}
