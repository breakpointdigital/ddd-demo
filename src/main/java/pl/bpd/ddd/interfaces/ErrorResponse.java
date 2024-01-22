package pl.bpd.ddd.interfaces;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(String message, Map<String, String> errors) {
}
