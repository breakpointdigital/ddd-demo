package pl.bpd.ddd.domain.ticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.bpd.ddd.domain.shared.spec.DomainSpecification;

import java.util.Optional;

public interface TicketRepository {
    Optional<Ticket> findById(TicketId id);

    Page<Ticket> findAll(Pageable pageable);

    Page<Ticket> findAll(DomainSpecification<Ticket> spec, Pageable pageable);

    void save(Ticket ticket);

    Optional<TicketId> findLastTicketId();
}
