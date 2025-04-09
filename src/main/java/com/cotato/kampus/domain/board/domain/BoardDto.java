package com.cotato.kampus.domain.board.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.board.dao.entity.BoardEntity;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

public record BoardDto(
	Long boardId,
	String boardName,
	String description,
	Long universityId,
	Boolean usesCategories,
	BoardStatus boardStatus,
	BoardType boardType,
	LocalDateTime deletionScheduledAt
) {
	public static BoardDto from(BoardEntity boardEntity) {
		return new BoardDto(
			boardEntity.getId(),
			boardEntity.getBoardName(),
			boardEntity.getDescription(),
			boardEntity.getUniversityId(),
			boardEntity.getUsesCategories(),
			boardEntity.getBoardStatus(),
			boardEntity.getBoardType(),
			boardEntity.getDeletionScheduledAt()
		);
	}
}
