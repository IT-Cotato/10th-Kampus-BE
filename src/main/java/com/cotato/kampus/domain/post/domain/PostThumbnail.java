package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 게시글 목록 조회에 사용 (게시판 이름 미포함)
 */

public record PostThumbnail(
	Long postId,
	String title,
	String content,
	int likeCount,
	int commentCount,
	int scrapCount,
	String thumbnailUrl,
	boolean isScrapped,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime
) {
	public static PostThumbnail from(Post post, String thumbnailUrl, boolean isScrapped) {
		return new PostThumbnail(
			post.getId(),
			post.getTitle(),
			post.getContent(),
			post.getLikeCount(),
			post.getCommentCount(),
			post.getScrapCount(),
			thumbnailUrl,
			isScrapped,
			post.getCreatedTime()
		);
	}
}