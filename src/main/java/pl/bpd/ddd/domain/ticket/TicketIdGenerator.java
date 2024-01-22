package pl.bpd.ddd.domain.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Domain service
 */
@Service
@RequiredArgsConstructor
public class TicketIdGenerator {
    private final TicketRepository ticketRepository;

    public TicketId generateNext() {
        int previousTicketNumber = ticketRepository.findLastTicketId()
                .map(TicketId::getTicketNumber)
                .orElse(0);
        return new TicketId("TT-" + (previousTicketNumber + 1));
    }
}
