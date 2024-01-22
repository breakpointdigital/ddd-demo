package pl.bpd.ddd.application.ticket.dto;

import pl.bpd.ddd.application.shared.CurrentUserInfo;

public record OpenTicketInput(String title, String description, CurrentUserInfo currentUserInfo) {
}
