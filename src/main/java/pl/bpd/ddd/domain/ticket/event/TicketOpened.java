package pl.bpd.ddd.domain.ticket.event;

import pl.bpd.ddd.domain.member.Assignee;
import pl.bpd.ddd.domain.member.Reporter;
import pl.bpd.ddd.domain.shared.DomainEvent;
import pl.bpd.ddd.domain.ticket.TicketId;

public record TicketOpened(TicketId ticketId, String title, Assignee assignee,
                           Reporter reporter) implements DomainEvent {
}
