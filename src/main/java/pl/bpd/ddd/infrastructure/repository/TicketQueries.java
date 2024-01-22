package pl.bpd.ddd.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.bpd.ddd.domain.ticket.Ticket;
import pl.bpd.ddd.domain.ticket.TicketId;

public interface TicketQueries extends JpaRepository<Ticket, TicketId>, JpaSpecificationExecutor<Ticket> {
}
