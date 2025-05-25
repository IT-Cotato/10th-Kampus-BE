package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.enums.PostStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NormalPost extends Post {

	@Builder(access = AccessLevel.PRIVATE)
	private NormalPost(
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

	public static NormalPost create(Long boardId, Long userId, String title, String content, PostStatus postStatus,
		Anonymity anonymity) {
		return NormalPost.builder()
			.boardId(boardId)
			.userId(userId)
			.title(title)
			.content(content)
			.postStatus(postStatus)
			.anonymity(anonymity)
			.likeCount(0)
			.commentCount(0)
			.scrapCount(0)
			.anonymousCount(0)
			.build();
	}

	public static NormalPost fromEntity(Long id, Long boardId, Long userId, String title, String content, PostStatus postStatus,
		Anonymity anonymity, int likeCount, int commentCount, int scrapCount, int anonymousCount,
		LocalDateTime createdTime, LocalDateTime lastModifiedTime) {
		return NormalPost.builder()
			.id(id)
			.boardId(boardId)
			.userId(userId)
			.title(title)
			.content(content)
			.postStatus(postStatus)
			.anonymity(anonymity)
			.likeCount(likeCount)
			.commentCount(commentCount)
			.scrapCount(scrapCount)
			.anonymousCount(anonymousCount)
			.createdTime(createdTime)
			.lastModifiedTime(lastModifiedTime)
			.build();
	}

	@Override
	protected NormalPost createCopy(String title, String content, Anonymity anonymity, PostStatus postStatus,
		int likeCount, int commentCount, int scrapCount, int anonymousCount) {
		return NormalPost.builder()
			.id(getId())
			.boardId(getBoardId())
			.userId(getUserId())
			.title(title)
			.content(content)
			.anonymity(anonymity)
			.postStatus(postStatus)
			.likeCount(likeCount)
			.commentCount(commentCount)
			.scrapCount(scrapCount)
			.anonymousCount(anonymousCount)
			.createdTime(getCreatedTime())
			.lastModifiedTime(LocalDateTime.now())
			.build();
	}
}
