package com.cotato.kampus.domain.board.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NormalBoard extends Board {

	@Builder(access = AccessLevel.PRIVATE)
	private NormalBoard(
		Long id,
		String boardName,
		String description,
		Boolean usesCategories,
		BoardStatus boardStatus,
		BoardType boardType,
		LocalDateTime deletionScheduledAt
	) {
		super(id, boardName, description, usesCategories, boardStatus, boardType, deletionScheduledAt);
	}

	public static NormalBoard create(String boardName, String description, Boolean usesCategories,
		BoardStatus boardStatus, BoardType boardType) {
		return NormalBoard.builder()
			.boardName(boardName)
			.description(description)
			.usesCategories(usesCategories)
			.boardStatus(boardStatus)
			.boardType(boardType)
			.build();
	}

	public static NormalBoard fromEntity(Long id, String boardName, String description, Boolean usesCategories,
		BoardStatus boardStatus, BoardType boardType, LocalDateTime deletionScheduledAt) {
		return NormalBoard.builder()
			.id(id)
			.boardName(boardName)
			.description(description)
			.usesCategories(usesCategories)
			.boardStatus(boardStatus)
			.boardType(boardType)
			.deletionScheduledAt(deletionScheduledAt)
			.build();
	}

	@Override
	public Board withUpdateInfo(String boardName, String description, Boolean usesCategories) {
		return NormalBoard.builder()
			.id(this.getId())
			.boardName(boardName)
			.description(description)
			.usesCategories(usesCategories)
			.boardStatus(this.getBoardStatus())
			.boardType(this.getBoardType())
			.deletionScheduledAt(this.getDeletionScheduledAt())
			.build();
	}

	@Override
	public Board withBoardStatus(BoardStatus boardStatus) {
		return NormalBoard.builder()
			.id(this.getId())
			.boardName(this.getBoardName())
			.description(this.getDescription())
			.usesCategories(this.getUsesCategories())
			.boardStatus(boardStatus)
			.boardType(this.getBoardType())
			.deletionScheduledAt(this.getDeletionScheduledAt())
			.build();
	}

	@Override
	public Board withPendingInfo(BoardStatus boardStatus, LocalDateTime deletionScheduledAt) {
		return NormalBoard.builder()
			.id(this.getId())
			.boardName(this.getBoardName())
			.description(this.getDescription())
			.usesCategories(this.getUsesCategories())
			.boardStatus(boardStatus)
			.boardType(this.getBoardType())
			.deletionScheduledAt(deletionScheduledAt)
			.build();
	}
}
