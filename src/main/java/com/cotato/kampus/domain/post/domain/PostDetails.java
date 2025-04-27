package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record PostDetails(
	Long postId,
	Long boardId,
	String title,
	String content,
	int likes,
	int scraps,
	int comments,
	List<PostPhotoInfo> postPhotos,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime,
	boolean isAuthor,
	boolean isLiked,
	boolean isScrapped
) {
	public static PostDetails of(
		Post post,
		List<PostPhoto> postPhotos,
		boolean isAuthor,
		boolean isLiked,
		boolean isScrapped
	) {
		return new PostDetails(
			post.getId(),
			post.getBoardId(),
			post.getTitle(),
			post.getContent(),
			post.getLikeCount(),
			post.getScrapCount(),
			post.getCommentCount(),
			postPhotos.stream()
				.map(PostPhotoInfo::from)
				.toList(),
			post.getCreatedTime(),
			isAuthor,
			isLiked,
			isScrapped
		);
	}
}