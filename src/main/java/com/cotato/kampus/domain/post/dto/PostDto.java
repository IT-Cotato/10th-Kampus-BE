package com.cotato.kampus.domain.post.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.enums.PostCategory;

public record PostDto(
	Long id,
	Long boardId,
	LocalDateTime createdTime,
	Long userId,
	String title,
	String content,
	Long likes,
	Anonymity anonymity,
	PostCategory postCategory,
	Long comments
) {
	public static PostDto from(Post post) {
		return new PostDto(
			post.getId(),
			post.getBoardId(),
			post.getCreatedTime(),
			post.getUserId(),
			post.getTitle(),
			post.getContent(),
			post.getLikes(),
			post.getAnonymity(),
			post.getPostCategory(),
			post.getComments()
		);
	}
}