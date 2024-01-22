package pl.bpd.ddd.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.bpd.ddd.domain.ticket.Comment;
import pl.bpd.ddd.domain.ticket.TicketId;

public interface CommentQueries extends JpaRepository<Comment, Comment.Id> {
    Page<Comment> findByTicketId(TicketId ticketId, Pageable pageable);
}
