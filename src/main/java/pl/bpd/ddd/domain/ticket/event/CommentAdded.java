package pl.bpd.ddd.domain.ticket.event;

import pl.bpd.ddd.domain.shared.DomainEvent;
import pl.bpd.ddd.domain.ticket.Comment;

public record CommentAdded(Comment.Id commentId) implements DomainEvent {
}
