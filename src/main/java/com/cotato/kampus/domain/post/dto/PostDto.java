package com.cotato.kampus.domain.post.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.enums.PostCategory;
import com.cotato.kampus.domain.post.enums.PostStatus;

public record PostDto(
	Long id,
	Long userId,
	Long boardId,
	String title,
	String content,
	Long likes,
	Long scraps,
	Long comments,
	Anonymity anonymity,
	PostStatus postStatus,
	PostCategory postCategory,
	Long nextAnonymousNumber,
	LocalDateTime createdTime
) {
	public static PostDto from(Post post) {
		return new PostDto(
			post.getId(),
			post.getUserId(),
			post.getBoardId(),
			post.getTitle(),
			post.getContent(),
			post.getLikes(),
			post.getScraps(),
			post.getComments(),
			post.getAnonymity(),
			post.getPostStatus(),
			post.getPostCategory(),
			post.getNextAnonymousNumber(),
			post.getCreatedTime()
		);
	}
}