package pl.bpd.ddd.interfaces.ticket.dto;

import jakarta.validation.constraints.NotBlank;

public record EditTicketTitleRequest(@NotBlank String title) {
}
