package pl.bpd.ddd.application.comment.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.bpd.ddd.application.comment.CommentService;
import pl.bpd.ddd.application.shared.events.DomainEventHandler;
import pl.bpd.ddd.domain.ticket.event.TicketOpened;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddInitialCommentEventHandler implements DomainEventHandler<TicketOpened> {
    private final CommentService commentService;

    @Override
    @EventListener
    public void handle(TicketOpened event) {
        log.info("Adding comment");
        commentService.createInitialComment(event.ticketId().id(), event.reporter());
    }
}
