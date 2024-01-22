package pl.bpd.ddd.domain.shared;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Validators {
    public static void validateNotBlank(CharSequence value, String field) {
        if (StringUtils.isBlank(value)) {
            throw new ValidationResultException(field, "must not be blank");
        }
    }

    public static void validateRegex(String value, String regex, String field) {
        if (value == null || !value.matches(regex)) {
            throw new ValidationResultException(field, "must match " + regex + " regex");
        }
    }

    public static void validateNotNull(Object value, String field) {
        if (value == null) {
            throw new ValidationResultException(field, "must not be null");
        }
    }

    public static void validateGte(double val, double min, String field) {
        if (val < min) {
            throw new ValidationResultException(field, "must be >= " + min);
        }
    }

    public static <T extends Number> void validateLt(double val, double lessThan, String field, String message) {
        if (val >= lessThan) {
            throw new ValidationResultException(field, message);
        }
    }

    public static Group group() {
        return new Group();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Group {
        private final ValidationResult validationResult = new ValidationResult();

        public Group notBlank(CharSequence value, String field) {
            if (StringUtils.isBlank(value)) {
                validationResult.addError(field, "must not be blank");
            }

            return this;
        }

        public Group notNull(Object value, String field) {
            if (value == null) {
                validationResult.addError(field, "must not be null");
            }

            return this;
        }

        public ValidationResult getResult() {
            return validationResult;
        }

        public Group throwIfNotValid() {
            if (validationResult.isInvalid()) {
                throw new ValidationResultException(validationResult);
            }

            return this;
        }
    }
}