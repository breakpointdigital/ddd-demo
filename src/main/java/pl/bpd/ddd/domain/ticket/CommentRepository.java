package pl.bpd.ddd.domain.ticket;

import com.github.f4b6a3.ulid.Ulid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.bpd.ddd.domain.shared.IdentityGenerator;

import java.util.Optional;

public interface CommentRepository extends IdentityGenerator<Comment.Id> {
    Optional<Comment> findById(Comment.Id id);

    Page<Comment> findForTicket(TicketId ticketId, Pageable pageable);

    void save(Comment comment);

    default Comment.Id nextId() {
        return new Comment.Id(Ulid.fast().toLowerCase());
    }
}
