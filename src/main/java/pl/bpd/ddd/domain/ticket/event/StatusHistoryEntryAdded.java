package pl.bpd.ddd.domain.ticket.event;

import pl.bpd.ddd.domain.member.Member;
import pl.bpd.ddd.domain.shared.DomainEvent;
import pl.bpd.ddd.domain.ticket.TicketStatus;

import java.time.Instant;

public record StatusHistoryEntryAdded(TicketStatus previousStatus, TicketStatus newStatus, Member editor,
                                      Instant changeDate) implements DomainEvent {
}
