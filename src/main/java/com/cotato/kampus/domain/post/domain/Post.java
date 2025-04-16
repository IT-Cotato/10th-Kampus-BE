package com.cotato.kampus.domain.post.domain;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.enums.PostStatus;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.Builder;
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
	// private final Long likes;
	// private final Long scraps;
	// private final Long comments;
	private final Anonymity anonymity;
	private final PostStatus postStatus;
	private final PostType postType;
	// private final Long nextAnonymousNumber;

	protected void validate() {
		validateBoardId();
		validateUserId();
		validatePostStatus();

		if(postStatus != PostStatus.DRAFT) {
			validateForPublishing();
		}
	}

	protected void validateBoardId() {
		if (boardId == null) {
			throw new AppException(ErrorCode.POST_BOARD_ID_REQUIRED);
		}
	}

	protected void validateUserId() {
		if (userId == null) {
			throw new AppException(ErrorCode.POST_AUTHOR_ID_REQUIRED);
		}
	}

	protected void validatePostStatus() {
		if(postStatus == null) {
			throw new AppException(ErrorCode.POST_STATUS_EMPTY);
		}
	}

	protected void validateForPublishing() {
		validateTitle();
		validatePostType();
		validateAnonymous();
	}

	protected void validateTitle() {
		if (title == null || title.trim().isEmpty()) {
			throw new AppException(ErrorCode.POST_TITLE_EMPTY);
		}
	}

	protected void validatePostType() {
		if (postType == null) {
			throw new AppException(ErrorCode.POST_TYPE_EMPTY);
		}
	}

	protected void validateAnonymous() {
		if (anonymity == null) {
			throw new AppException(ErrorCode.POST_ANONYMOUS_EMPTY);
		}
	}

	public abstract Post withUpdateInfo(String title, String content);
	public abstract Post withPostStatus(PostStatus postStatus);

	//
	// @Builder
	// public Post(Long userId, Long boardId, String title, String content, Anonymity anonymity){
	// 	this.userId = userId;
	// 	this.boardId = boardId;
	// 	this.title = title;
	// 	this.content = content;
	// 	this.anonymity = anonymity;
	// }
	//
	// public void update(String title, String content) {
	// 	this.title = title;
	// 	this.content = content;
	// }
	//
	// public void increaseNextAnonymousNumber() {
	// 	this.nextAnonymousNumber++;
	// }
	//
	// public void increaseScraps() {
	// 	this.scraps++;
	// }
	//
	// public void decreaseScraps() {
	// 	this.scraps--;
	// }
	//
	// public void increaseLikes() {
	// 	this.likes++;
	// }
	//
	// public void decreaseLikes() {
	// 	this.likes--;
	// }
	//
	// public void increaseComments() {
	// 	this.comments++;
	// }
	//
	// public void decreaseComments() {
	// 	this.comments--;
	// }
	//
	// public void updateStatus(PostStatus postStatus) {
	// 	this.postStatus = postStatus;
	// }
}
