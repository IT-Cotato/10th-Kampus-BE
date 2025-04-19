package com.cotato.kampus.domain.board.application;

import org.springframework.stereotype.Service;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.implement.boardFavorite.BoardFavoriteAppender;
import com.cotato.kampus.domain.board.implement.boardFavorite.BoardFavoriteDeleter;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.board.implement.board.BoardValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BoardFavoriteService {

	private final BoardFinder boardFinder;
	private final BoardValidator boardValidator;

	private final BoardFavoriteAppender boardFavoriteAppender;
	private final BoardFavoriteDeleter boardFavoriteDeleter;

	public Long addFavoriteBoard(Long boardId) {
		// 게시판 조회
		Board board = boardFinder.findBoard(boardId);

		// 게시판 검증
		boardValidator.validateBoardIsActive(board);

		// 즐겨찾기 추가
		return boardFavoriteAppender.appendFavoriteBoard(board.getId());
	}

	public Long removeFavoriteBoard(Long boardId) {
		boardFavoriteDeleter.deleteFavoriteBoard(boardId);
		return boardId;
	}
}
