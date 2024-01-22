package pl.bpd.ddd.domain.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationResult {
    private final List<ValidationError> errors = new ArrayList<>();

    public ValidationResult addError(ValidationError error) {
        errors.add(error);
        return this;
    }

    public ValidationResult addError(String key, String message, Object... args) {
        errors.add(new ValidationError(key, message, args));
        return this;
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public boolean isInvalid() {
        return !isValid();
    }

    public List<ValidationError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public static ValidationResult ofError(String key, String message, Object... args) {
        var result = new ValidationResult();
        return result.addError(key, message, args);
    }

    @Override
    public String toString() {
        var strErrors = errors.stream()
                .map(ValidationError::getFormattedMessage)
                .collect(Collectors.joining(", "));
        if (strErrors.isBlank()) {
            strErrors = "success";
        }
        return "ValidationResult: " + strErrors;
    }
}
