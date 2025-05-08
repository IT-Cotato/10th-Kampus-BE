package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.cotato.kampus.domain.post.enums.PostStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

public record PostDetails(
	Long postId,
	Long boardId,
	String boardName,
	String title,
	String content,
	int likeCount,
	int scrapCount,
	int commentCount,
	PostStatus postStatus,
	List<PostPhotoInfo> postPhotos,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime,
	boolean isAuthor,
	boolean isLiked,
	boolean isScrapped
) {
	public static PostDetails of(
		Post post,
		String boardName,
		List<PostPhoto> postPhotos,
		boolean isAuthor,
		boolean isLiked,
		boolean isScrapped
	) {
		return new PostDetails(
			post.getId(),
			post.getBoardId(),
			boardName,
			post.getTitle(),
			post.getContent(),
			post.getLikeCount(),
			post.getScrapCount(),
			post.getCommentCount(),
			post.getPostStatus(),
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