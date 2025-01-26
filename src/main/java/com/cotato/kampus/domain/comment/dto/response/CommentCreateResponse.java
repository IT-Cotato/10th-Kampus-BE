package com.cotato.kampus.domain.comment.dto.response;

public record CommentCreateResponse(
	Long commentId
) {
	public static CommentCreateResponse of(Long commentId) {
		return new CommentCreateResponse(commentId);
	}
}
