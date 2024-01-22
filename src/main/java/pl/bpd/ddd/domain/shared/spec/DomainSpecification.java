package pl.bpd.ddd.domain.shared.spec;

import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.stream.StreamSupport;

public interface DomainSpecification<T> {
    boolean isSatisfiedBy(T entity);

    Specification<T> toQuery();

    default DomainSpecification<T> and(DomainSpecification<T> other) {
        return new AndSpecification<>(this, other);
    }

    default DomainSpecification<T> andNot(DomainSpecification<T> other) {
        return new AndNotSpecification<>(this, other);
    }

    default DomainSpecification<T> or(DomainSpecification<T> other) {
        return new OrSpecification<>(this, other);
    }

    default DomainSpecification<T> orNot(DomainSpecification<T> other) {
        return new OrNotSpecification<>(this, other);
    }

    default DomainSpecification<T> not() {
        return new NotSpecification<>(this);
    }

    static <T> DomainSpecification<T> allOf(Iterable<DomainSpecification<T>> specifications) {
        return StreamSupport.stream(specifications.spliterator(), false)
                .reduce(new EmptySpec<>(), DomainSpecification::and);
    }

    @SafeVarargs
    static <T> DomainSpecification<T> allOf(DomainSpecification<T>... specifications) {
        return allOf(Arrays.asList(specifications));
    }

    static <T> DomainSpecification<T> anyOf(Iterable<DomainSpecification<T>> specifications) {
        return StreamSupport.stream(specifications.spliterator(), false)
                .reduce(new EmptySpec<>(), DomainSpecification::or);
    }

    @SafeVarargs
    static <T> DomainSpecification<T> anyOf(DomainSpecification<T>... specifications) {
        return anyOf(Arrays.asList(specifications));
    }
}

class EmptySpec<T> implements DomainSpecification<T> {
    @Override
    public boolean isSatisfiedBy(T entity) {
        return true;
    }

    @Override
    public Specification<T> toQuery() {
        return Specification.where(null);
    }
}

class AndSpecification<T> implements DomainSpecification<T> {
    private final DomainSpecification<T> left;
    private final DomainSpecification<T> right;

    public AndSpecification(DomainSpecification<T> left, DomainSpecification<T> right) {
        this.left = left;
        this.right = right;
    }


    @Override
    public boolean isSatisfiedBy(T candidate) {
        return left.isSatisfiedBy(candidate) && right.isSatisfiedBy(candidate);
    }

    @Override
    public Specification<T> toQuery() {
        return left.toQuery().and(right.toQuery());
    }
}

class AndNotSpecification<T> implements DomainSpecification<T> {
    private final DomainSpecification<T> left;
    private final DomainSpecification<T> right;

    public AndNotSpecification(DomainSpecification<T> left, DomainSpecification<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfiedBy(T candidate) {
        return left.isSatisfiedBy(candidate) && !right.isSatisfiedBy(candidate);
    }

    @Override
    public Specification<T> toQuery() {
        return left.toQuery().and(Specification.not(right.toQuery()));
    }
}

class OrSpecification<T> implements DomainSpecification<T> {
    private final DomainSpecification<T> left;
    private final DomainSpecification<T> right;

    public OrSpecification(DomainSpecification<T> left, DomainSpecification<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfiedBy(T candidate) {
        return left.isSatisfiedBy(candidate) || right.isSatisfiedBy(candidate);
    }

    @Override
    public Specification<T> toQuery() {
        return left.toQuery().or(right.toQuery());
    }
}

class OrNotSpecification<T> implements DomainSpecification<T> {
    private final DomainSpecification<T> left;
    private final DomainSpecification<T> right;

    public OrNotSpecification(DomainSpecification<T> left, DomainSpecification<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfiedBy(T candidate) {
        return left.isSatisfiedBy(candidate) || !right.isSatisfiedBy(candidate);
    }

    @Override
    public Specification<T> toQuery() {
        return left.toQuery().or(Specification.not(right.toQuery()));
    }
}

class NotSpecification<T> implements DomainSpecification<T> {
    private final DomainSpecification<T> other;

    public NotSpecification(DomainSpecification<T> other) {
        this.other = other;
    }

    @Override
    public boolean isSatisfiedBy(T candidate) {
        return !other.isSatisfiedBy(candidate);
    }

    @Override
    public Specification<T> toQuery() {
        return Specification.not(other.toQuery());
    }
}