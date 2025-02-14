package com.cotato.kampus.domain.comment.dto.request;

public record CommentCreateRequest(
	String content,
	Long parentId
) {
}
