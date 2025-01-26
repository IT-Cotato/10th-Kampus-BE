package com.cotato.kampus.domain.comment.dto.response;

public record CommentLikeResponse(
	Long likeId
) {

	public static CommentLikeResponse of(Long likeId) {

		return new CommentLikeResponse(likeId);
	}

}
