package com.cotato.kampus.domain.admin.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.category.domain.Category;
import com.fasterxml.jackson.annotation.JsonFormat;

public record BoardDetails(
	Long boardId,
	String boardName,
	String description,
	String universityName,
	Boolean usesCategories,
	BoardStatus boardStatus,
	BoardType boardType,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime deletionScheduledAt,
	List<Category> categories
) {
	public static BoardDetails from(Board board, String universityName, List<Category> categories){
		return new BoardDetails(
			board.getId(),
			board.getBoardName(),
			board.getDescription(),
			universityName,
			board.getUsesCategories(),
			board.getBoardStatus(),
			board.getBoardType(),
			board.getDeletionScheduledAt(),
			categories
		);
	}
}
