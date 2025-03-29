package com.cotato.kampus.domain.post.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostPhoto;
import com.fasterxml.jackson.annotation.JsonFormat;

public record SearchedPost(
	Long id,
	Long boardId,
	String boardName,
	String title,
	String content,
	Long likes,
	Long comments,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime,
	String thumbnailUrl
) {
	public static SearchedPost of(Post post, String boardName, PostPhoto postPhoto) {
		return new SearchedPost(
			post.getId(),
			post.getBoardId(),
			boardName,
			post.getTitle(),
			post.getContent(),
			post.getLikes(),
			post.getComments(),
			post.getCreatedTime(),
			postPhoto != null ? postPhoto.getPhotoUrl() : null
		);
	}
}