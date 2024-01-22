package pl.bpd.ddd.application.shared.events;

import pl.bpd.ddd.domain.shared.DomainEvent;

public interface DomainEventNotificationHandler<E extends DomainEvent> {
    void handle(DomainEventNotification<E> event);
}
