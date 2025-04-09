package com.cotato.kampus.domain.board.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class Board {

	private final Long id;
	private final String boardName;
	private final String description;
	private final Long universityId;
	private final Boolean usesCategories;
	private final BoardStatus boardStatus;
	private final BoardType boardType;
	private final LocalDateTime deletionScheduledAt;

	public Board withUpdateInfo(String boardName, String description, Boolean usesCategories) {
		return Board.builder()
			.id(this.id)
			.boardName(boardName)
			.description(description)
			.universityId(this.universityId)
			.usesCategories(usesCategories)
			.boardStatus(this.boardStatus)
			.boardType(this.boardType)
			.deletionScheduledAt(this.deletionScheduledAt)
			.build();
	}

	public Board withBoardStatus(BoardStatus boardStatus) {
		return Board.builder()
			.id(this.id)
			.boardName(this.boardName)
			.description(this.description)
			.universityId(this.universityId)
			.usesCategories(this.usesCategories)
			.boardStatus(boardStatus)
			.boardType(this.boardType)
			.deletionScheduledAt(this.deletionScheduledAt)
			.build();
	}

	public Board withPendingInfo(BoardStatus boardStatus, LocalDateTime deletionScheduledAt) {
		return Board.builder()
			.id(this.id)
			.boardName(this.boardName)
			.description(this.description)
			.universityId(this.universityId)
			.usesCategories(this.usesCategories)
			.boardStatus(boardStatus)
			.boardType(this.boardType)
			.deletionScheduledAt(deletionScheduledAt)
			.build();
	}
}