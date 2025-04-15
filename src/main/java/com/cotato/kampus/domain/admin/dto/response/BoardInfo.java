package com.cotato.kampus.domain.admin.dto.response;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

public record BoardInfo(
	Long boardId,
	String boardName,
	String description,
	String universityName,
	Boolean usesCategories,
	BoardStatus boardStatus,
	BoardType boardType
) {
	public static BoardInfo from(Board board, String universityName){
		return new BoardInfo(
			board.getId(),
			board.getBoardName(),
			board.getDescription(),
			universityName,
			board.getUsesCategories(),
			board.getBoardStatus(),
			board.getBoardType()
		);
	}
}
