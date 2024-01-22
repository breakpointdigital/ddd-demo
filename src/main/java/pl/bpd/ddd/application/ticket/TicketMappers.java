package pl.bpd.ddd.application.ticket;

import org.mapstruct.Mapper;
import pl.bpd.ddd.application.ticket.dto.ChecklistItemOutput;
import pl.bpd.ddd.application.ticket.dto.TicketDetailsOutput;
import pl.bpd.ddd.application.ticket.dto.TicketSummaryOutput;
import pl.bpd.ddd.domain.shared.EntityId;
import pl.bpd.ddd.domain.ticket.ChecklistItem;
import pl.bpd.ddd.domain.ticket.Ticket;

@Mapper
public interface TicketMappers {
    TicketSummaryOutput ticketToSummary(Ticket ticket);

    TicketDetailsOutput ticketToDetails(Ticket ticket);

    ChecklistItemOutput checklistItemToSummary(ChecklistItem checklistItem);

    default String map(EntityId id) {
        return id.id();
    }
}
