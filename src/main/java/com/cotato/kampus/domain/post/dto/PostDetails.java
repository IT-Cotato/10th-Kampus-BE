package com.cotato.kampus.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetails(
	AnonymousOrPostAuthor postAuthor,
	LocalDateTime createdTime,
	String title,
	String content,
	Long likes,
	Long comments,
	List<String> postPhotos
) {
	public static PostDetails of(AnonymousOrPostAuthor postAuthor, PostDto postDto, List<String> postPhotos) {
		return new PostDetails(
			postAuthor,
			postDto.createdTime(),
			postDto.title(),
			postDto.content(),
			postDto.likes(),
			postDto.comments(),
			postPhotos
		);
	}
}