package com.cotato.kampus.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.cotato.kampus.domain.post.enums.PostCategory;

public record PostDetails(
	AnonymousOrPostAuthor postAuthor,
	LocalDateTime createdTime,
	Long boardId,
	String title,
	String content,
	Long likes,
	Long comments,
	PostCategory postCategory,
	List<String> postPhotos
) {
	public static PostDetails of(AnonymousOrPostAuthor postAuthor, PostDto postDto, List<String> postPhotos) {
		return new PostDetails(
			postAuthor,
			postDto.createdTime(),
			postDto.boardId(),
			postDto.title(),
			postDto.content(),
			postDto.likes(),
			postDto.comments(),
			postDto.postCategory(),
			postPhotos
		);
	}
}