package com.cotato.kampus.domain.board.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class Board {

	private final Long id;
	private final String boardName;
	private final String description;
	private final Boolean usesCategories;
	private final BoardStatus boardStatus;
	private final BoardType boardType;
	private final LocalDateTime deletionScheduledAt;

	protected void validate() {
		validateBoardName();
		validateDescription();
		validateBoardStatus();
		validateBoardType();
	}

	private void validateBoardName() {
		if(boardName == null || boardName.trim().isEmpty()) {
			throw new AppException(ErrorCode.BOARD_NAME_EMPTY);
		}
	}

	private void validateDescription() {
		if(description == null || description.trim().isEmpty()) {
			throw new AppException(ErrorCode.BOARD_DESCRIPTION_EMPTY);
		}
		if(description.length() > 90) {
			throw new AppException(ErrorCode.BOARD_DESCRIPTION_TOO_LONG);
		}
	}

	private void validateBoardStatus() {
		if(boardStatus == null) {
			throw new AppException(ErrorCode.BOARD_STATUS_EMPTY);
		}
	}

	private void validateBoardType() {
		if(boardType == null) {
			throw new AppException(ErrorCode.BOARD_STATUS_EMPTY);
		}
	}

	public abstract Board withUpdateInfo(String boardName, String description, Boolean usesCategories);
	public abstract Board withBoardStatus(BoardStatus boardStatus);
	public abstract Board withPendingInfo(BoardStatus boardStatus, LocalDateTime deletionScheduledAt);
}