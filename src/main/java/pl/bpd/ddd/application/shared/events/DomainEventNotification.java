package pl.bpd.ddd.application.shared.events;

import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public record DomainEventNotification<T>(T source) implements ResolvableTypeProvider {
    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(
                getClass(),
                ResolvableType.forInstance(this.source)
        );
    }
}
