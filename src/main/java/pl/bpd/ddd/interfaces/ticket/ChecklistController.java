package pl.bpd.ddd.interfaces.ticket;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bpd.ddd.application.ticket.TicketService;
import pl.bpd.ddd.interfaces.ticket.dto.CheckChecklistItemRequest;
import pl.bpd.ddd.interfaces.ticket.dto.EditChecklistItemRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets/{ticketId}")
public class ChecklistController {
    private final TicketService ticketService;

    @PostMapping
    public void addChecklistItem(@PathVariable String ticketId, @RequestBody @Valid EditChecklistItemRequest dto) {
        ticketService.addChecklistItem(ticketId, dto.content());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/checklist/{itemId}")
    public void editChecklistItem(@PathVariable String ticketId, @PathVariable String itemId, @RequestBody @Valid EditChecklistItemRequest dto) {
        ticketService.editChecklistItem(ticketId, itemId, dto.content());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/checklist/{itemId}")
    public void deleteChecklistItem(@PathVariable String ticketId, @PathVariable String itemId) {
        ticketService.deleteChecklistItem(ticketId, itemId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/checklist/{itemId}/switch")
    public void switchChecklistItem(@PathVariable String ticketId, @PathVariable String itemId, @RequestBody @Valid CheckChecklistItemRequest dto) {
        ticketService.checkChecklistItem(
                ticketId,
                itemId,
                dto.checked());
    }
}
