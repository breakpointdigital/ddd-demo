package pl.bpd.ddd.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.bpd.ddd.domain.ticket.Comment;
import pl.bpd.ddd.domain.ticket.CommentRepository;
import pl.bpd.ddd.domain.ticket.TicketId;

import java.util.Optional;

@Primary
@Repository
@RequiredArgsConstructor
public class JpaCommentRepository implements CommentRepository {
    private final CommentQueries commentQueries;

    @Override
    public Optional<Comment> findById(Comment.Id id) {
        return commentQueries.findById(id);
    }

    @Override
    public Page<Comment> findForTicket(TicketId ticketId, Pageable pageable) {
        return commentQueries.findByTicketId(ticketId, pageable);
    }

    @Override
    public void save(Comment comment) {
        commentQueries.save(comment);
    }
}
