package pl.bpd.ddd.domain.ticket.event;

import pl.bpd.ddd.domain.shared.DomainEvent;
import pl.bpd.ddd.domain.ticket.TicketId;

public record TicketTitleUpdated(TicketId ticketId, String title) implements DomainEvent {
}
