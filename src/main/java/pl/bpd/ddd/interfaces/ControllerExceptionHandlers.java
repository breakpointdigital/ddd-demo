package pl.bpd.ddd.interfaces;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.bpd.ddd.application.shared.exceptions.EntityNotFoundException;
import pl.bpd.ddd.domain.shared.ValidationError;
import pl.bpd.ddd.domain.shared.ValidationResultException;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandlers {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationResultException.class)
    public ErrorResponse handleValidationResultException(ValidationResultException e) {
        log.debug("Validation exception", e);

        Map<String, String> errors = e.getValidationResult().getErrors()
                .stream()
                .collect(Collectors.toMap(ValidationError::getKey, ValidationError::getFormattedMessage));

        return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentValidationException(MethodArgumentNotValidException ex) {
        log.debug("Method argument validation exception", ex);

        Function<ObjectError, String> getErrorMessage = error -> Objects.toString(error.getDefaultMessage(), "");

        Map<String, String> errors = Stream.concat(
                ex.getFieldErrors().stream()
                        .map(error -> Pair.of(error.getField(), getErrorMessage.apply(error))),
                ex.getGlobalErrors().stream()
                        .map(error -> Pair.of(error.getObjectName(), getErrorMessage.apply(error)))
        ).collect(Collectors.toMap(Pair::getFirst, Pair::toString));

        return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
    }
}
