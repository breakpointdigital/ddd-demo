package pl.bpd.ddd.application.shared.events;

import pl.bpd.ddd.domain.shared.DomainEvent;

public interface DomainEventHandler<E extends DomainEvent> {
    void handle(E event);
}
