package pl.bpd.ddd.interfaces.ticket.dto;

import jakarta.validation.constraints.NotBlank;

public record OpenTicketRequest(
        @NotBlank
        String title,
        @NotBlank
        String description) {
}
