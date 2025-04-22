package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.enums.PostStatus;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NormalPost extends Post {
	private final String content;
	private final Anonymity anonymity;

	@Builder
	public NormalPost(
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
		LocalDateTime lastModifiedTime,

		// 추가 필드
		String content,
		Anonymity anonymity    // 추후 확장 가능성
	) {
		super(id, boardId, userId, title, postStatus, likeCount, commentCount, scrapCount, anonymousCount, createdTime,
			lastModifiedTime);
		this.content = content;
		this.anonymity = anonymity;
		validate();
		validateContent();
		validateAnonymous();
	}

	private void validateContent() {
		if (getContent() == null || getContent().trim().isEmpty()) {
			throw new AppException(ErrorCode.POST_CONTENT_EMPTY);
		}
		if (getContent().length() > 1000) {
			throw new AppException(ErrorCode.POST_CONTENT_TOO_LONG);
		}
	}

	private void validateAnonymous() {
		if (anonymity == null) {
			throw new AppException(ErrorCode.POST_ANONYMOUS_EMPTY);
		}
	}

	public NormalPost withUpdateInfo(String title, String content, Anonymity anonymity) {
		return NormalPost.builder()
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

	public NormalPost withPostStatus(PostStatus postStatus) {
		return NormalPost.builder()
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
