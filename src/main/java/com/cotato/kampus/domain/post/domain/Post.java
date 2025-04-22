package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;

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
	private final PostStatus postStatus;
	private final int likeCount;
	private final int commentCount;
	private final int scrapCount;
	private final int anonymousCount;
	private final LocalDateTime createdTime;
	private final LocalDateTime lastModifiedTime;

	protected void validate() {
		validateBoardId();
		validateUserId();
		validateTitle();
		validatePostStatus();
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

	protected void validateTitle() {
		if (title == null || title.trim().isEmpty()) {
			throw new AppException(ErrorCode.POST_TITLE_EMPTY);
		}
	}

	protected void validatePostStatus() {
		if (postStatus == null) {
			throw new AppException(ErrorCode.POST_STATUS_EMPTY);
		}
	}
}
