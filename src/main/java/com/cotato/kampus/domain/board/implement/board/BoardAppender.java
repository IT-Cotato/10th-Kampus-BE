package com.cotato.kampus.domain.board.implement.board;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.domain.NormalBoard;
import com.cotato.kampus.domain.board.domain.UniversityBoard;
import com.cotato.kampus.domain.board.implement.port.BoardRepository;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardAppender {
	private final BoardRepository boardRepository;
	private final BoardValidator boardValidator;

	@Transactional
	public Board appendBoard(String boardName, String description, BoardType boardType, Long universityId,
		Boolean usesCategories) {

		boardValidator.validateBoardTypeAndUniversityId(boardType, universityId);

		Board board;

		if(boardType == BoardType.UNIVERSITY) {
			board = UniversityBoard.builder()
				.boardName(boardName)
				.description(description)
				.usesCategories(usesCategories)
				.boardStatus(BoardStatus.ACTIVE)
				.universityId(universityId)
				.build();
		} else {
			board = NormalBoard.builder()
				.boardName(boardName)
				.description(description)
				.usesCategories(usesCategories)
				.boardStatus(BoardStatus.ACTIVE)
				.build();
		}

		return boardRepository.save(board);
	}
}
