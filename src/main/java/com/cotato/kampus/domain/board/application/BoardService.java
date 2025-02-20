package com.cotato.kampus.domain.board.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.board.dto.BoardWithFavoriteStatus;
import com.cotato.kampus.domain.board.dto.HomeBoardAndPostPreview;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.application.PostDtoMapper;
import com.cotato.kampus.domain.post.application.PostFinder;
import com.cotato.kampus.domain.post.dto.PostDto;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.dto.UserDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BoardService {

	private final BoardFinder boardFinder;
	private final BoardValidator boardValidator;
	private final BoardDtoEnhancer boardDtoEnhancer;
	private final BoardFavoriteReader boardFavoriteReader;
	private final BoardFavoriteAppender boardFavoriteAppender;
	private final BoardFavoriteDeleter boardFavoriteDeleter;
	private final UserValidator userValidator;
	private final ApiUserResolver apiUserResolver;
	private final PostFinder postFinder;
	private final PostDtoMapper postDtoMapper;

	public List<BoardWithFavoriteStatus> getBoardList() {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 즐겨찾는 게시판 조회
		List<Long> favoriteBoardIds = boardFavoriteReader.findFavoriteBoardIds(userId);

		// 공용 게시판 조회
		List<BoardDto> boards = boardFinder.findPublicBoards();

		// 즐겨찾기 여부 매핑
		List<BoardWithFavoriteStatus> boardWithFavorites = new ArrayList<>(
			boardDtoEnhancer.updateFavoriteStatus(boards, favoriteBoardIds));

		// 즐겨찾기 게시판이 위로 오도록 정렬
		boardWithFavorites.sort(Comparator.comparing(BoardWithFavoriteStatus::isFavorite).reversed());

		return boardWithFavorites;
	}

	public List<HomeBoardAndPostPreview> getFavoriteBoardPreview() {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 즐겨찾는 게시판 조회
		List<Long> favoriteBoardIds = boardFavoriteReader.findFavoriteBoardIds(userId);
		List<BoardDto> boardDtos = boardFinder.findBoardDtos(favoriteBoardIds);

		return postDtoMapper.mapToHomeBoardAndPostPreviewsByBoardDtos(boardDtos);
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

	public BoardDto getUniversityBoard() {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 재학생 인증 확인
		Long userUniversityId = userValidator.validateStudentVerification(userId);

		// 대학교 게시판 조회
		return boardFinder.findUserUniversityBoard(userUniversityId);
	}

	public Boolean requiresCategory(Long boardId) {
		BoardDto boardDto = boardFinder.findBoardDto(boardId);

		return boardDto.isCategoryRequired();
	}

	public BoardWithFavoriteStatus getBoard(Long boardId) {
		// 유저 조회
		UserDto userDto = apiUserResolver.getCurrentUserDto();

		BoardDto boardDto = boardFinder.findBoardDto(boardId);

		return boardDtoEnhancer.mapToBoardWithFavoriteStatus(boardDto, userDto);
	}

	public List<HomeBoardAndPostPreview> getTrendingPreview() {
		// 유저 정보 조회
		UserDto userDto = apiUserResolver.getCurrentUserDto();
		Long userUnivId = userDto.universityId();

		// Trending 게시글 조회 (타 대학 게시글 제외)
		List<PostDto> trendingPosts = postFinder.findTrendingPosts(userUnivId);

		return postDtoMapper.mapToHomeBoardAndPostPreviews(trendingPosts);
	}
}
