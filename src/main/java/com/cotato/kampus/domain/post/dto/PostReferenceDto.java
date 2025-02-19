package com.cotato.kampus.domain.post.dto;

import com.cotato.kampus.domain.post.domain.Post;

public record PostReferenceDto(
	Long postId,
	String title,
	Long boardId,
	boolean isDeleted
) {
	public static PostReferenceDto from(Post post) {
		return new PostReferenceDto(
			post.getId(),
			post.getTitle(),
			post.getBoardId(),
			false
		);
	}

	public static PostReferenceDto deleted() {
		return new PostReferenceDto(-1L, "", -1L, true);
	}
}