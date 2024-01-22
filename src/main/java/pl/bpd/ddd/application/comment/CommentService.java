package pl.bpd.ddd.application.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bpd.ddd.application.comment.dto.CommentOutput;
import pl.bpd.ddd.application.shared.CurrentUserInfo;
import pl.bpd.ddd.application.shared.exceptions.EntityNotFoundException;
import pl.bpd.ddd.domain.member.Member;
import pl.bpd.ddd.domain.member.MemberRepository;
import pl.bpd.ddd.domain.member.Reporter;
import pl.bpd.ddd.domain.ticket.*;

/**
 * Application service
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final MemberRepository memberRepository;
    private final CommentMapper commentMapper;

    public CommentOutput addComment(String ticketId, String content, CurrentUserInfo currentUserInfo) {
        Ticket ticket = ticketRepository.findById(new TicketId(ticketId))
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.fromUserInfo(currentUserInfo);
        Comment comment = ticket.comment(member, content);
        commentRepository.save(comment);
        return commentMapper.commentToOutput(comment);
    }

    public void editComment(String commentId, String content) {
        Comment comment = commentRepository.findById(new Comment.Id(commentId))
                .orElseThrow(EntityNotFoundException::new);

        Ticket ticket = ticketRepository.findById(comment.getTicketId())
                .orElseThrow(EntityNotFoundException::new);

        ticket.editComment(comment, content);
        commentRepository.save(comment);
    }

    public void createInitialComment(String ticketId, Reporter reporter) {
        Ticket ticket = ticketRepository.findById(new TicketId(ticketId))
                .orElseThrow(EntityNotFoundException::new);

        Comment comment = ticket.comment(reporter, "I created this ticket!");
        commentRepository.save(comment);
    }

    public Page<CommentOutput> getCommentsForTicket(String ticketId, Pageable pageable) {
        return commentRepository.findForTicket(new TicketId(ticketId), pageable)
                .map(commentMapper::commentToOutput);
    }
}
