package pl.bpd.ddd.domain.ticket;

import com.github.f4b6a3.ulid.Ulid;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import pl.bpd.ddd.domain.member.Member;
import pl.bpd.ddd.domain.shared.DomainEntity;
import pl.bpd.ddd.domain.shared.EntityId;
import pl.bpd.ddd.domain.shared.ValidationResultException;
import pl.bpd.ddd.domain.ticket.event.CommentAdded;

import java.time.Instant;

// aggregate root
@Entity
@Getter
@EqualsAndHashCode(callSuper = false)
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends DomainEntity {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private Id id;
    private TicketId ticketId;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;
    private String content;
    private Instant createdAt;

    static Comment create(TicketId ticketId, Member author, String content) {
        if (author == null) {
            throw new ValidationResultException("author", "must not be null");
        }
        if (StringUtils.isBlank(content)) {
            throw new ValidationResultException("content", "must not be blank");
        }

        var comment = new Comment(
                new Id(Ulid.fast().toLowerCase()),
                ticketId,
                author,
                content,
                Instant.now()
        );

        comment.addDomainEvent(new CommentAdded(comment.getId()));

        return comment;
    }

    void edit(String content) {
        if (StringUtils.isBlank(content)) {
            throw new ValidationResultException("content", "must not be blank");
        }
        this.content = content;
    }

    @Embeddable
    @AllArgsConstructor
    @EqualsAndHashCode
    @NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
    static public class Id implements EntityId {
        @Column(name = "comment_id")
        private String id;

        @Override
        public String id() {
            return id;
        }
    }
}
