package com.cotato.kampus.domain.comment.dto.response;

public record CommentDeleteResponse(
	Long commentId
) {
	public static CommentDeleteResponse of(Long commentId) {
		return new CommentDeleteResponse(commentId);
	}
}
