package com.cotato.kampus.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.cotato.kampus.domain.post.enums.PostCategory;

public record PostDetails(
	Long postId,
	Long boardId,
	String title,
	String content,
	PostCategory postCategory,
	Long likes,
	Long scraps,
	Long comments,
	List<String> postPhotoUrls,
	LocalDateTime createdTime,
	boolean isAuthor,
	boolean isLiked,
	boolean isScrapped
) {
	public static PostDetails of(
		PostDto postDto,
		List<String> postPhotoUrls,
		boolean isAuthor,
		boolean isLiked,
		boolean isScrapped
	) {
		return new PostDetails(
			postDto.id(),
			postDto.boardId(),
			postDto.title(),
			postDto.content(),
			postDto.postCategory(),
			postDto.likes(),
			postDto.scraps(),
			postDto.comments(),
			postPhotoUrls,
			postDto.createdTime(),
			isAuthor,
			isLiked,
			isScrapped
		);
	}
}