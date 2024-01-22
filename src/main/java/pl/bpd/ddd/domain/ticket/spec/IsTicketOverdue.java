package pl.bpd.ddd.domain.ticket.spec;

import org.springframework.data.jpa.domain.Specification;
import pl.bpd.ddd.domain.shared.spec.DomainSpecification;
import pl.bpd.ddd.domain.ticket.Ticket;
import pl.bpd.ddd.domain.ticket.TicketStatus;
import pl.bpd.ddd.domain.ticket.Ticket_;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Check if a ticket is overdue, i.e. it was created more than 7 days ago and is not closed.
 * Business requirement - find all tickets that are not completed for too long.
 */
public class IsTicketOverdue implements DomainSpecification<Ticket> {
    private static final Duration overdueAfter = Duration.of(7, ChronoUnit.DAYS);

    private final Clock clock;

    public IsTicketOverdue() {
        clock = Clock.systemUTC();
    }

    IsTicketOverdue(Clock clock) {
        this.clock = clock;
    }

    @Override
    public boolean isSatisfiedBy(Ticket ticket) {
        if (ticket.getCreatedDate().isBefore(calculateOverdueBefore())
            && !ticket.getStatus().isClosed()) {
            return true;
        }

        return false;
    }

    @Override
    public Specification<Ticket> toQuery() {
        return (root, query, cb) ->
                cb.and(
                        cb.lessThan(
                                root.get(Ticket_.createdDate),
                                calculateOverdueBefore()
                        ),
                        cb.notEqual(root.get(Ticket_.status), TicketStatus.CLOSED)
                );

    }

    private Instant calculateOverdueBefore() {
        return ZonedDateTime.now(clock).minus(overdueAfter).toInstant();
    }
}
