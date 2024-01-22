package pl.bpd.ddd.interfaces.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record AddCommentRequest(@NotBlank String content) {
}
