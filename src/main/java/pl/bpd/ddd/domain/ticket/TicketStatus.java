package pl.bpd.ddd.domain.ticket;

import java.util.Set;

public enum TicketStatus {
    RECEIVED {
        @Override
        protected Set<TicketStatus> allowedStatusesTransitions() {
            return Set.of(VERIFIED, CLOSED);
        }
    },
    VERIFIED {
        @Override
        protected Set<TicketStatus> allowedStatusesTransitions() {
            return Set.of(PRIORITIZE, CLOSED);
        }
    },
    PRIORITIZE {
        @Override
        protected Set<TicketStatus> allowedStatusesTransitions() {
            return Set.of(IN_PROGRESS, SUSPENDED, CLOSED);
        }
    },
    IN_PROGRESS {
        @Override
        protected Set<TicketStatus> allowedStatusesTransitions() {
            return Set.of(PRIORITIZE, SUSPENDED, CLOSED);
        }
    },
    SUSPENDED {
        @Override
        protected Set<TicketStatus> allowedStatusesTransitions() {
            return Set.of(PRIORITIZE, IN_PROGRESS, CLOSED);
        }
    },
    CLOSED {
        @Override
        protected Set<TicketStatus> allowedStatusesTransitions() {
            return Set.of();
        }
    };

    protected abstract Set<TicketStatus> allowedStatusesTransitions();

    public boolean canChangeTo(TicketStatus nextStatus) {
        return allowedStatusesTransitions().contains(nextStatus);
    }

    public boolean isClosed() {
        return this == CLOSED;
    }
}
