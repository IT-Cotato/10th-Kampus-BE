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

	public abstract Board withUpdateInfo(String boardName, String description, Boolean usesCategories);
	public abstract Board withBoardStatus(BoardStatus boardStatus);
	public abstract Board withPendingInfo(BoardStatus boardStatus, LocalDateTime deletionScheduledAt);

	public void validateActive() {
		if(boardStatus != BoardStatus.ACTIVE)
			throw new AppException(ErrorCode.BOARD_NOT_ACTIVE);
	}
}