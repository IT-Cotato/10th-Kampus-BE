package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.common.enums.Anonymity;
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
		String content,
		PostStatus postStatus,
		Anonymity anonymity, // 추후 확장 가능
		int likeCount,
		int commentCount,
		int scrapCount,
		int anonymousCount,
		LocalDateTime createdTime,
		LocalDateTime lastModifiedTime
	) {
		super(id, boardId, userId, title, content, postStatus, anonymity, likeCount, commentCount, scrapCount,
			anonymousCount, createdTime, lastModifiedTime);
		validate();
	}

	@Override
	public CardNewsPost withUpdateInfo(String title, String content, Anonymity anonymity) {
		return CardNewsPost.builder()
			.id(getId())
			.boardId(getBoardId())
			.userId(getUserId())
			.title(title)
			.content(content)
			.anonymity(anonymity)
			.postStatus(getPostStatus())
			.likeCount(this.getLikeCount())
			.commentCount(this.getCommentCount())
			.scrapCount(this.getScrapCount())
			.anonymousCount(this.getAnonymousCount())
			.createdTime(this.getCreatedTime())
			.lastModifiedTime(LocalDateTime.now())
			.build();
	}

	@Override
	public CardNewsPost withPostStatus(PostStatus postStatus) {
		return CardNewsPost.builder()
			.id(getId())
			.boardId(getBoardId())
			.userId(getUserId())
			.title(getTitle())
			.content(getContent())
			.anonymity(getAnonymity())
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
