package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.enums.PostStatus;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class Post {

	private final Long id;
	private final Long boardId;
	private final Long userId;
	private final String title;
	private final String content;
	private final PostStatus postStatus;
	private final Anonymity anonymity;
	private final int likeCount;
	private final int commentCount;
	private final int scrapCount;
	private final int anonymousCount;
	private final LocalDateTime createdTime;
	private final LocalDateTime lastModifiedTime;

	protected abstract Post createCopy(String title, String content, Anonymity anonymity, PostStatus postStatus,
		int likeCount, int commentCount, int scrapCount, int anonymousCount);

	public Post withUpdateInfo(String title, String content, Anonymity anonymity) {
		return createCopy(title, content, anonymity, this.postStatus,
			this.likeCount, this.commentCount, this.scrapCount, this.anonymousCount);
	}

	public Post withPostStatus(PostStatus postStatus) {
		return createCopy(this.title, this.content, this.anonymity, postStatus,
			this.likeCount, this.commentCount, this.scrapCount, this.anonymousCount);
	}

	public Post increaseLikeCount() {
		return createCopy(this.title, this.content, this.anonymity, this.postStatus,
			this.likeCount + 1, this.commentCount, this.scrapCount, this.anonymousCount);
	}

	public Post decreaseLikeCount() {
		return createCopy(this.title, this.content, this.anonymity, this.postStatus,
			Math.max(0, this.likeCount - 1), this.commentCount, this.scrapCount, this.anonymousCount);
	}

	public Post increaseCommentCount() {
		return createCopy(this.title, this.content, this.anonymity, this.postStatus,
			this.likeCount, this.commentCount + 1, this.scrapCount, this.anonymousCount);
	}

	public Post decreaseCommentCount() {
		return createCopy(this.title, this.content, this.anonymity, this.postStatus,
			this.likeCount, Math.max(0, this.commentCount - 1), this.scrapCount, this.anonymousCount);
	}

	public Post increaseScrapCount() {
		return createCopy(this.title, this.content, this.anonymity, this.postStatus,
			this.likeCount, this.commentCount, this.scrapCount + 1, this.anonymousCount);
	}

	public Post decreaseScrapCount() {
		return createCopy(this.title, this.content, this.anonymity, this.postStatus,
			this.likeCount, this.commentCount, Math.max(0, this.scrapCount - 1), this.anonymousCount);
	}

	public Post increaseAnonymousCount() {
		return createCopy(this.title, this.content, this.anonymity, this.postStatus,
			this.likeCount, this.commentCount, this.scrapCount, this.anonymousCount + 1);
	}

	public Post increaseCommentAndAnonymousCount() {
		return createCopy(this.title, this.content, this.anonymity, this.postStatus,
			this.likeCount, this.commentCount + 1, this.scrapCount, this.anonymousCount + 1);
	}

	public void validateAuthor(Long userId) {
		if(!this.userId.equals(userId)) {
			throw new AppException(ErrorCode.POST_NOT_AUTHOR);
		}
	}

	public void validatePublish() {
		if(this.postStatus != PostStatus.PUBLISHED) {
			throw new AppException(ErrorCode.POST_NOT_PUBLISHED);
		}
	}
}
