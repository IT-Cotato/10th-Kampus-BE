package com.cotato.kampus.domain.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.cotato.kampus.domain.post.dto.PostDetails;
import com.cotato.kampus.domain.post.dto.PostDto;
import com.cotato.kampus.domain.post.enums.PostCategory;
import com.fasterxml.jackson.annotation.JsonFormat;

public record PostDetailResponse(
	Long postId,
	String title,
	String content,
	PostCategory postCategory,
	Long likes,
	Long scraps,
	Long comments,
	List<String> postPhotoUrls,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime,
	boolean isAuthor,
	boolean isLiked,
	boolean isScrapped
) {
	public static PostDetailResponse from(PostDetails postDetails) {
		return new PostDetailResponse(
			postDetails.postId(),
			postDetails.title(),
			postDetails.content(),
			postDetails.postCategory(),
			postDetails.likes(),
			postDetails.scraps(),
			postDetails.comments(),
			postDetails.postPhotoUrls(),
			postDetails.createdTime(),
			postDetails.isAuthor(),
			postDetails.isLiked(),
			postDetails.isScrapped()
		);
	}
}