package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TemporaryPost {

	private final Long id;
	private final Long boardId;
	private final Long userId;
	private final String title;
	private final String content;
	private final Anonymity anonymity;
	private final LocalDateTime createdTime;
	private final LocalDateTime lastModifiedTime;

	@Builder
	public TemporaryPost(
		Long id,
		Long boardId,
		Long userId,
		String title,
		String content,
		Anonymity anonymity,    // 추후 확장 가능성
		LocalDateTime createdTime,
		LocalDateTime lastModifiedTime
	) {
		this.id = id;
		this.boardId = boardId;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.anonymity = anonymity;
		this.createdTime = createdTime;
		this.lastModifiedTime = lastModifiedTime;
		validate();
	}

	private void validate() {
		validateBoardId();
		validateUserId();
	}

	private void validateBoardId() {
		if (boardId == null) {
			throw new AppException(ErrorCode.POST_BOARD_ID_REQUIRED);
		}
	}

	private void validateUserId() {
		if (userId == null) {
			throw new AppException(ErrorCode.POST_AUTHOR_ID_REQUIRED);
		}
	}

	public TemporaryPost withUpdateInfo(String title, String content, Anonymity anonymity) {
		return TemporaryPost.builder()
			.id(getId())
			.boardId(getBoardId())
			.userId(getUserId())
			.title(title)
			.content(content)
			.anonymity(anonymity)
			.createdTime(getCreatedTime())
			.lastModifiedTime(LocalDateTime.now())
			.build();
	}
}