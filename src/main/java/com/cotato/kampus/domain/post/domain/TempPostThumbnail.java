package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record TempPostThumbnail(
	Long tempPostId,
	Long boardId,
	String boardName,
	String title,
	String content,
	String thumbnailUrl,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime lastModifiedTime
) {
	public static TempPostThumbnail from(TemporaryPost tempPost, String boardName, String thumbnailUrl) {
		return new TempPostThumbnail(
			tempPost.getId(),
			tempPost.getBoardId(),
			boardName,
			tempPost.getTitle(),
			tempPost.getContent(),
			thumbnailUrl,
			tempPost.getCreatedTime(),
			tempPost.getLastModifiedTime()
		);
	}
}
