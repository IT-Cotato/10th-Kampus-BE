package com.cotato.kampus.domain.admin.dto.response;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonFormat;

public record AdminCardNewsThumbnail(
	Long postId,
	String title,
	String content,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime,
	String thumbnailUrl
) {
	public static AdminCardNewsThumbnail from(Post post, String thumbnailUrl) {
		return new AdminCardNewsThumbnail(
			post.getId(),
			post.getTitle(),
			post.getContent(),
			post.getCreatedTime(),
			thumbnailUrl
		);
	}
}
