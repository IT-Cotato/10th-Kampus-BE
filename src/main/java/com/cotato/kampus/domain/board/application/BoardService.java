package com.cotato.kampus.domain.board.application;

import java.util.ArrayList;
import java.util.Comparator;
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
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 즐겨찾는 게시판 조회
		Set<Long> favoriteBoardIds = boardFavoriteReader.findFavoriteBoardIds(userId);

		// 공용 게시판 조회
		List<BoardWithFavoriteStatusDto> boards = boardFinder.findPublicBoards();

		// 즐겨찾기 여부 매핑
		List<BoardWithFavoriteStatusDto> boardWithFavorites = new ArrayList<>(BoardDtoEnhancer.updateFavoriteStatus(boards, favoriteBoardIds));

		// 즐겨찾기 게시판이 위로 오도록 정렬
		boardWithFavorites.sort(Comparator.comparing(BoardWithFavoriteStatusDto::isFavorite).reversed());

		return boardWithFavorites;
	}

	public List<BoardWithFavoriteStatusDto> getFavoriteBoardList() {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 즐겨찾는 게시판 조회
		Set<Long> favoriteBoardIds = boardFavoriteReader.findFavoriteBoardIds(userId);

		// 전체 게시판 조회
		List<BoardWithFavoriteStatusDto> boards = boardFinder.findPublicBoards();

		// 즐겨찾는 게시판만 필터링
		return BoardDtoEnhancer.filterFavoriteBoards(boards, favoriteBoardIds);
	}

	public Long addFavoriteBoard(Long boardId) {
		// 게시판 조회
		BoardDto boardDto = boardFinder.findBoardDto(boardId);

		// 게시판 검증
		boardValidator.validateBoardIsActive(boardDto);

		// 즐겨찾기 추가
		return boardFavoriteAppender.appendFavoriteBoard(boardDto.boardId());
	}

	public Long removeFavoriteBoard(Long boardId) {
		boardFavoriteDeleter.deleteFavoriteBoard(boardId);
		return boardId;
	}

	public BoardDto getUniversityBoard(){
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 재학생 인증 확인
		Long userUniversityId = userValidator.validateStudentVerification(userId);

		// 대학교 게시판 조회
		return boardFinder.findUserUniversityBoard(userUniversityId);
	}

	public Boolean requiresCategory(Long boardId){
		BoardDto boardDto = boardFinder.findBoardDto(boardId);

		return boardDto.isCategoryRequired();
	}

	public BoardDto getBoard(Long boardId){
		return boardFinder.findBoardDto(boardId);
	}
}
