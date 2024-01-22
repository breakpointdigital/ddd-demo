package pl.bpd.ddd.interfaces.ticket.dto;

import jakarta.validation.constraints.NotBlank;

public record EditTicketDescriptionRequest(@NotBlank String description) {
}
