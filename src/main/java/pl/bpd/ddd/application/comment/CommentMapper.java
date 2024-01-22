package pl.bpd.ddd.application.comment;

import org.mapstruct.Mapper;
import pl.bpd.ddd.application.comment.dto.CommentOutput;
import pl.bpd.ddd.domain.shared.EntityId;
import pl.bpd.ddd.domain.ticket.Comment;

@Mapper
public interface CommentMapper {
    CommentOutput commentToOutput(Comment comment);

    default String map(EntityId id) {
        return id.id();
    }
}
