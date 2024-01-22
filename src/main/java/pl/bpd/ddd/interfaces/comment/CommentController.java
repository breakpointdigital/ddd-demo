package pl.bpd.ddd.interfaces.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.bpd.ddd.application.comment.CommentService;
import pl.bpd.ddd.application.comment.dto.CommentOutput;
import pl.bpd.ddd.application.shared.CurrentUserInfo;
import pl.bpd.ddd.interfaces.comment.dto.AddCommentRequest;
import pl.bpd.ddd.interfaces.comment.dto.EditCommentRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ticket/{ticketId}/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentOutput> addComment(@PathVariable String ticketId, @Valid @RequestBody AddCommentRequest dto, CurrentUserInfo currentUserInfo) {
        var comment = commentService.addComment(ticketId, dto.content(), currentUserInfo);
        var path = ServletUriComponentsBuilder.fromCurrentRequest()
                .pathSegment(comment.id())
                .build().toUri();
        return ResponseEntity.created(path)
                .body(comment);
    }

    @PutMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editComment(@Valid @RequestBody EditCommentRequest dto, @PathVariable String commentId) {
        commentService.editComment(commentId, dto.content());
    }
}
