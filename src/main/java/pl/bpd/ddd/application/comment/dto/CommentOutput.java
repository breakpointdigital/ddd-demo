package pl.bpd.ddd.application.comment.dto;

import pl.bpd.ddd.domain.member.Member;

public record CommentOutput(String id, String content, Member author) {
}
