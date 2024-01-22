package pl.bpd.ddd.application.ticket.dto;

import pl.bpd.ddd.domain.member.Assignee;
import pl.bpd.ddd.domain.member.Reporter;

public record OpenTicketOutput(String id, String title, Assignee assignee, Reporter reporter) {
}
