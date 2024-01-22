package pl.bpd.ddd.domain.shared;

import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DomainEntity {
    @Transient
    private transient final List<DomainEvent> domainEvents = new ArrayList<>();

    protected void addDomainEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
}
