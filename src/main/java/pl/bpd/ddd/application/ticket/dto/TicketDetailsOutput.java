package pl.bpd.ddd.application.ticket.dto;

import pl.bpd.ddd.domain.ticket.StatusHistoryEntry;

import java.util.List;

public record TicketDetailsOutput(String id, String title, List<ChecklistItemOutput> checklist,
                                  List<StatusHistoryEntry> statusHistory) {
}
