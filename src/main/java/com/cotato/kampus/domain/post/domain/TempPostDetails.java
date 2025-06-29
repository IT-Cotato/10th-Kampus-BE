package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.cotato.kampus.domain.category.domain.Category;
import com.fasterxml.jackson.annotation.JsonFormat;

public record TempPostDetails(
	Long tempPostId,
	Long boardId,
	String boardName,
	String title,
	String content,
	List<TempPhotoInfo> postPhotos,
	List<String> categoryNames,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime lastModifiedTime
) {
	public static TempPostDetails of(TemporaryPost tempPost, String boardName, List<TemporaryPhoto> postPhotos, List<Category> categories) {
		return new TempPostDetails(
			tempPost.getId(),
			tempPost.getBoardId(),
			boardName,
			tempPost.getTitle(),
			tempPost.getContent(),
			postPhotos.stream()
				.map(TempPhotoInfo::from)
				.toList(),
			categories.stream()
				.map(Category::getCategoryName)
				.toList(),
			tempPost.getCreatedTime(),
			tempPost.getLastModifiedTime()
		);
	}
}
