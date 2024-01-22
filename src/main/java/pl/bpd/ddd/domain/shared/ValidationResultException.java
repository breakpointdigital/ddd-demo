package pl.bpd.ddd.domain.shared;

import lombok.Getter;

@Getter
public class ValidationResultException extends RuntimeException {
    private final ValidationResult validationResult;

    public ValidationResultException(ValidationResult validationResult) {
        this(validationResult, null);
    }

    public ValidationResultException(String key, String message) {
        this(ValidationResult.ofError(key, message));
    }

    public ValidationResultException(ValidationResult validationResult, Throwable cause) {
        super(validationResult.toString(), cause);
        this.validationResult = validationResult;
    }
}
