package com.cotato.kampus.domain.post.dto.request;

public record PostCreateRequest(
	Long boardId,
	String title,
	String content,
	String postCategory
) {
}
