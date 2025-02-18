package com.cotato.kampus.domain.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.cotato.kampus.domain.post.dto.PostDetails;
import com.cotato.kampus.domain.post.enums.PostCategory;
import com.fasterxml.jackson.annotation.JsonFormat;

public record PostDetailResponse(
	Long userId,
	String username,
	Boolean isAuthor,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime,
	String title,
	String content,
	PostCategory postCategory,
	Long likes,
	Long comments,
	List<String> postPhotoUrls
) {
	public static PostDetailResponse from(PostDetails postDetails) {
		return new PostDetailResponse(
			postDetails.postAuthor().userId(),
			postDetails.postAuthor().username(),
			postDetails.postAuthor().isAuthor(),
			postDetails.createdTime(),
			postDetails.title(),
			postDetails.content(),
			postDetails.postCategory(),
			postDetails.likes(),
			postDetails.comments(),
			postDetails.postPhotos()
		);
	}
}