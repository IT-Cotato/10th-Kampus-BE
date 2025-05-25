package com.cotato.kampus.domain.board.application;

import org.springframework.stereotype.Service;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.domain.BoardFavorite;
import com.cotato.kampus.domain.board.implement.boardFavorite.BoardFavoriteFinder;
import com.cotato.kampus.domain.board.implement.boardFavorite.BoardFavoriteManager;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.board.implement.board.BoardValidator;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BoardFavoriteService {

	private final BoardFinder boardFinder;
	private final BoardValidator boardValidator;

	private final BoardFavoriteManager boardFavoriteManager;
	private final ApiUserResolver apiUserResolver;
	private final BoardFavoriteFinder boardFavoriteFinder;

	public Long addFavoriteBoard(Long boardId) {
		// 유저. 게시판 조회
		UserDto user = apiUserResolver.getCurrentUserDto();
		Board board = boardFinder.findBoard(boardId);

		// 게시판 검증
		boardValidator.validateBoardIsActive(board);
		boardValidator.validateUniversityAccess(user, board);

		// 즐겨찾기 중복 검증
		boolean isFavorite = boardFavoriteFinder.existsByUserIdAndBoardId(user.id(), boardId);
		if(isFavorite){
			throw new AppException(ErrorCode.BOARD_ALREADY_FAVORITED);
		}

		// 즐겨찾기 추가
		return boardFavoriteManager.appendFavoriteBoard(user.id(), board.getId());
	}

	public Long removeFavoriteBoard(Long boardId) {
		// 유저 조회
		UserDto user = apiUserResolver.getCurrentUserDto();

		// 즐겨찾기 조회/삭제
		BoardFavorite boardFavorite = boardFavoriteFinder.findByUserIdAndBoardId(user.id(), boardId);
		boardFavoriteManager.deleteFavoriteBoard(boardFavorite);

		return boardId;
	}
}
