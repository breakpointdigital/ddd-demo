package pl.bpd.ddd.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.bpd.ddd.domain.shared.spec.DomainSpecification;
import pl.bpd.ddd.domain.ticket.Ticket;
import pl.bpd.ddd.domain.ticket.TicketId;
import pl.bpd.ddd.domain.ticket.TicketRepository;

import java.util.Optional;

@Primary
@Repository
@Transactional
@RequiredArgsConstructor
public class JpaTicketRepository implements TicketRepository {
    private final TicketQueries ticketQueries;
    private final EntityManager entityManager;
    private final DomainEventDispatcher domainEventDispatcher;

    @Override
    public Optional<Ticket> findById(TicketId id) {
        return ticketQueries.findById(id);
    }

    @Override
    public Page<Ticket> findAll(Pageable pageable) {
        return ticketQueries.findAll(pageable);
    }

    @Override
    public Page<Ticket> findAll(DomainSpecification<Ticket> spec, Pageable pageable) {
        return ticketQueries.findAll(spec.toQuery(), pageable);
    }

    @Override
    public void save(Ticket ticket) {
        ticketQueries.save(ticket);
        domainEventDispatcher.publishEntity(ticket);
    }

    @Override
    public Optional<TicketId> findLastTicketId() {
        try {
            var query = entityManager.createQuery("select t.id from Ticket t order by length(t.id.id) desc, t.id.id desc ", TicketId.class);
            query.setMaxResults(1);
            TicketId result = query.getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
