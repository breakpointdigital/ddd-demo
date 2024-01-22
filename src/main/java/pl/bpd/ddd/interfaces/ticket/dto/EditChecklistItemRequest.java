package pl.bpd.ddd.interfaces.ticket.dto;

import jakarta.validation.constraints.NotBlank;

public record EditChecklistItemRequest(@NotBlank String content) {
}
