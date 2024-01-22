package pl.bpd.ddd.application.ticket.handler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.bpd.ddd.application.shared.events.DomainEventHandler;
import pl.bpd.ddd.domain.ticket.event.CommentAdded;

@Component
public class LogNewCommentEventHandler implements DomainEventHandler<CommentAdded> {
    @Override
    @EventListener
    public void handle(CommentAdded event) {
        System.out.println("comment added!");
    }
}
