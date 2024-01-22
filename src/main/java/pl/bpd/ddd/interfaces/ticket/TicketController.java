package pl.bpd.ddd.interfaces.ticket;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.bpd.ddd.application.comment.CommentService;
import pl.bpd.ddd.application.comment.dto.CommentOutput;
import pl.bpd.ddd.application.shared.CurrentUserInfo;
import pl.bpd.ddd.application.ticket.TicketService;
import pl.bpd.ddd.application.ticket.dto.OpenTicketOutput;
import pl.bpd.ddd.application.ticket.dto.TicketDetailsOutput;
import pl.bpd.ddd.application.ticket.dto.TicketSummaryOutput;
import pl.bpd.ddd.interfaces.ticket.dto.EditTicketDescriptionRequest;
import pl.bpd.ddd.interfaces.ticket.dto.EditTicketTitleRequest;
import pl.bpd.ddd.interfaces.ticket.dto.OpenTicketRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<OpenTicketOutput> openTicket(@RequestBody @Valid OpenTicketRequest dto, CurrentUserInfo currentUserInfo) {
        var ticket = ticketService.openTicket(dto.title(), dto.description(), currentUserInfo);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .pathSegment(ticket.id())
                .build().toUri();
        return ResponseEntity.created(uri)
                .body(ticket);
    }

    @GetMapping("/{ticketId}")
    public TicketDetailsOutput getTicket(@PathVariable String ticketId) {
        return ticketService.getTicket(ticketId);
    }

    @GetMapping
    public Page<TicketSummaryOutput> getAllTickets(@PageableDefault Pageable pageable, CurrentUserInfo currentUserInfo) {
        System.out.println(currentUserInfo);
        return ticketService.getAllTickets(pageable);
    }

    @GetMapping("/overdue")
    public Page<TicketSummaryOutput> getOverdueTickets(@PageableDefault Pageable pageable) {
        return ticketService.getOverdueTickets(pageable);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{ticketId}/title")
    public void editTitle(@PathVariable String ticketId, @RequestBody @Valid EditTicketTitleRequest dto, CurrentUserInfo currentUserInfo) {
        ticketService.editTitle(ticketId, dto.title());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{ticketId}/description")
    public void editDescription(@PathVariable String ticketId, @RequestBody @Valid EditTicketDescriptionRequest dto, CurrentUserInfo currentUserInfo) {
        ticketService.editDescription(ticketId, dto.description());
    }

    @GetMapping("/{ticketId}/comments")
    public Page<CommentOutput> getComments(@PathVariable String ticketId, @PageableDefault Pageable pageable) {
        return commentService.getCommentsForTicket(ticketId, pageable);
    }
}
