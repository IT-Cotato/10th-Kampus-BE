package com.cotato.kampus.domain.board.application;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.board.dto.BoardWithFavoriteStatusDto;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.user.application.UserValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BoardService {

	private final BoardFinder boardFinder;
	private final BoardValidator boardValidator;
	private final BoardFavoriteReader boardFavoriteReader;
	private final BoardFavoriteAppender boardFavoriteAppender;
	private final BoardFavoriteDeleter boardFavoriteDeleter;
	private final UserValidator userValidator;
	private final ApiUserResolver apiUserResolver;

	public List<BoardWithFavoriteStatusDto> getBoardList(){

		// 즐겨찾는 게시판 조회
		Set<Long> favoriteBoardIds = boardFavoriteReader.read();

		// 공용 게시판 조회
		List<BoardWithFavoriteStatusDto> boards = boardFinder.findPublicBoards();

		// 즐겨찾기 여부 매핑
		return BoardDtoEnhancer.updateFavoriteStatus(boards, favoriteBoardIds);
	}

	public List<BoardWithFavoriteStatusDto> getFavoriteBoardList() {
		// 즐겨찾는 게시판 조회
		Set<Long> favoriteBoardIds = boardFavoriteReader.read();

		// 전체 게시판 조회
		List<BoardWithFavoriteStatusDto> boards = boardFinder.findPublicBoards();

		// 즐겨찾는 게시판만 필터링
		return BoardDtoEnhancer.filterFavoriteBoards(boards, favoriteBoardIds);
	}

	public Long addFavoriteBoard(Long boardId) {
		// 게시판이 존재하는지 확인
		boardValidator.validateBoardExists(boardId);

		// 즐겨찾기 추가
		return boardFavoriteAppender.appendFavoriteBoard(boardId);
	}

	public Long removeFavoriteBoard(Long boardId) {
		boardFavoriteDeleter.deleteFavoriteBoard(boardId);
		return boardId;
	}

	public BoardDto getUniversityBoard(){
		// 유저 조회
		Long userId = apiUserResolver.getUserId();

		// 재학생 인증 확인
		Long userUniversityId = userValidator.validateStudentVerification(userId);

		// 대학교 게시판 조회
		return boardFinder.findUserUniversityBoard(userUniversityId);
	}

	public Boolean requiresCategory(Long boardId){
		BoardDto boardDto = boardFinder.findBoard(boardId);

		return boardDto.isCategoryRequired();
	}

	public BoardDto getBoard(Long boardId){
		return boardFinder.findBoard(boardId);
	}
}
