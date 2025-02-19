package com.cotato.kampus.domain.admin.dto.response;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.post.dto.PostWithPhotos;
import com.fasterxml.jackson.annotation.JsonFormat;

public record AdminCardNewsPreview(
	Long postId,
	String title,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime,
	String thumbnailUrl
) {
	public static AdminCardNewsPreview from(PostWithPhotos postWithPhotos) {
		return new AdminCardNewsPreview(
			postWithPhotos.id(),
			postWithPhotos.title(),
			postWithPhotos.createdTime(),
			postWithPhotos.thumbnailUrl()
		);
	}

}
