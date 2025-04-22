package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.post.enums.PostStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CardNewsPost extends Post {

	@Builder
	public CardNewsPost(
		Long id,
		Long boardId,
		Long userId,
		String title,
		PostStatus postStatus,
		int likeCount,
		int commentCount,
		int scrapCount,
		int anonymousCount,
		LocalDateTime createdTime,
		LocalDateTime lastModifiedTime
	) {
		super(id, boardId, userId, title, postStatus, likeCount, commentCount, scrapCount, anonymousCount, createdTime,
			lastModifiedTime);
		validate();
	}

	public CardNewsPost withUpdateInfo(String title) {
		return CardNewsPost.builder()
			.id(this.getId())
			.boardId(this.getBoardId())
			.userId(this.getUserId())
			.title(title)
			.postStatus(this.getPostStatus())
			.likeCount(this.getLikeCount())
			.commentCount(this.getCommentCount())
			.scrapCount(this.getScrapCount())
			.anonymousCount(this.getAnonymousCount())
			.createdTime(this.getCreatedTime())
			.lastModifiedTime(LocalDateTime.now())
			.build();
	}

	public CardNewsPost withPostStatus(PostStatus postStatus) {
		return CardNewsPost.builder()
			.id(this.getId())
			.boardId(this.getBoardId())
			.userId(this.getUserId())
			.title(this.getTitle())
			.postStatus(postStatus)
			.likeCount(this.getLikeCount())
			.commentCount(this.getCommentCount())
			.scrapCount(this.getScrapCount())
			.anonymousCount(this.getAnonymousCount())
			.createdTime(this.getCreatedTime())
			.lastModifiedTime(LocalDateTime.now())
			.build();
	}
}
