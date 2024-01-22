package pl.bpd.ddd.domain.shared;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;

@ToString
@EqualsAndHashCode
public class ValidationError {
    private final String key;
    private final String message;
    private final Object[] allArgs;

    public ValidationError(String key, String message, Object... args) {
        this.key = key;
        this.message = message;
        var mergedArgs = new ArrayList<>();
        mergedArgs.add(key);
        mergedArgs.addAll(Arrays.asList(args));
        this.allArgs = mergedArgs.toArray();
    }

    public String getKey() {
        return key;
    }

    public String getFormattedMessage() {
        return MessageFormat.format(message, allArgs);
    }
}
