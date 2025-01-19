package com.cotato.kampus.domain.post.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.domain.Post;

public record PostDto(
	Long id,
	LocalDateTime createdTime,
	Long userId,
	String title,
	String content,
	Long likes,
	Anonymity anonymity,
	Long comments
) {
	public static PostDto from(Post post) {
		return new PostDto(
			post.getId(),
			post.getCreatedTime(),
			post.getUserId(),
			post.getTitle(),
			post.getContent(),
			post.getLikes(),
			post.getAnonymity(),
			post.getComments()
		);
	}
}