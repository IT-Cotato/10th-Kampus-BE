package com.cotato.kampus.domain.board.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UniversityBoard extends Board {

	private final Long universityId;

	@Builder(access = AccessLevel.PACKAGE)
	UniversityBoard(
		Long id,
		String boardName,
		String description,
		Boolean usesCategories,
		BoardStatus boardStatus,
		BoardType boardType,
		Long universityId,
		LocalDateTime deletionScheduledAt
	) {
		super(id, boardName, description, usesCategories, boardStatus, boardType, deletionScheduledAt);
		this.universityId = universityId;
		validateUniversityId();
	}

	public static UniversityBoard create(String boardName, String description, Boolean usesCategories,
		BoardStatus boardStatus, BoardType boardType, Long universityId) {
		return UniversityBoard.builder()
			.boardName(boardName)
			.description(description)
			.usesCategories(usesCategories)
			.boardStatus(boardStatus)
			.boardType(boardType)
			.universityId(universityId)
			.build();
	}

	public static UniversityBoard fromEntity(Long id, String boardName, String description, Boolean usesCategories,
		BoardStatus boardStatus, BoardType boardType, Long universityId, LocalDateTime deletionScheduledAt) {
		return UniversityBoard.builder()
			.id(id)
			.boardName(boardName)
			.description(description)
			.usesCategories(usesCategories)
			.boardStatus(boardStatus)
			.boardType(boardType)
			.universityId(universityId)
			.deletionScheduledAt(deletionScheduledAt)
			.build();
	}

	@Override
	public Board withUpdateInfo(String boardName, String description, Boolean usesCategories) {
		return UniversityBoard.builder()
			.id(this.getId())
			.boardName(boardName)
			.description(description)
			.usesCategories(usesCategories)
			.boardStatus(this.getBoardStatus())
			.deletionScheduledAt(this.getDeletionScheduledAt())
			.universityId(this.universityId)
			.build();
	}

	@Override
	public Board withBoardStatus(BoardStatus boardStatus) {
		return UniversityBoard.builder()
			.id(this.getId())
			.boardName(this.getBoardName())
			.description(this.getDescription())
			.usesCategories(this.getUsesCategories())
			.boardStatus(boardStatus)
			.deletionScheduledAt(this.getDeletionScheduledAt())
			.universityId(this.universityId)
			.build();
	}

	@Override
	public Board withPendingInfo(BoardStatus boardStatus, LocalDateTime deletionScheduledAt) {
		return UniversityBoard.builder()
			.id(this.getId())
			.boardName(this.getBoardName())
			.description(this.getDescription())
			.usesCategories(this.getUsesCategories())
			.boardStatus(boardStatus)
			.deletionScheduledAt(deletionScheduledAt)
			.universityId(this.universityId)
			.build();
	}

	private void validateUniversityId() {
		if (universityId == null) {
			throw new AppException(ErrorCode.BOARD_UNIVERSITY_ID_REQUIRED);
		}
	}
}
