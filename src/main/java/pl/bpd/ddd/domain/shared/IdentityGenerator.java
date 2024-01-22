package pl.bpd.ddd.domain.shared;

public interface IdentityGenerator<ID extends EntityId> {
    ID nextId();
}
