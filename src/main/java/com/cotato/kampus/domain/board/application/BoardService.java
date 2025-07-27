package com.cotato.kampus.domain.board.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.domain.BoardWithFavoriteStatus;
import com.cotato.kampus.domain.board.domain.HomePostThumbnail;
import com.cotato.kampus.domain.board.implement.board.BoardDtoMapper;
import com.cotato.kampus.domain.board.implement.boardFavorite.BoardFavoriteFinder;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.implement.post.PostDtoMapper;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.post.implement.trendingPost.TrendingPostFinder;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.dto.UserDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BoardService {

	private final BoardFinder boardFinder;
	private final BoardDtoMapper boardDtoMapper;
	private final BoardFavoriteFinder boardFavoriteFinder;
	private final UserValidator userValidator;
	private final ApiUserResolver apiUserResolver;
	private final PostDtoMapper postDtoMapper;
	private final TrendingPostFinder trendingPostFinder;
	private final PostFinder postFinder;

	public List<BoardWithFavoriteStatus> getBoardList() {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 즐겨찾는 게시판 조회
		List<Long> favoriteBoardIds = boardFavoriteFinder.findFavoriteBoardIds(userId);

		// 공용 게시판 조회
		List<Board> boards = boardFinder.findPublicBoards();

		// 즐겨찾기 여부 매핑
		List<BoardWithFavoriteStatus> boardWithFavorites = new ArrayList<>(
			boardDtoMapper.updateFavoriteStatus(boards, favoriteBoardIds));

		// 즐겨찾기 게시판이 위로 오도록 정렬
		boardWithFavorites.sort(Comparator.comparing(BoardWithFavoriteStatus::isFavorite).reversed());

		return boardWithFavorites;
	}

	public List<HomePostThumbnail> getFavoriteBoardPreview() {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 즐겨찾는 게시판 조회
		List<Long> favoriteBoardIds = boardFavoriteFinder.findFavoriteBoardIds(userId);
		Map<Long, Board> boards = boardFinder.findBoardMap(favoriteBoardIds);

		Map<Long, Optional<Post>> latestPosts = postFinder.findTopPosts(favoriteBoardIds);

		return postDtoMapper.toHomePostThumbnails(boards, latestPosts);
	}

	public BoardWithFavoriteStatus getUniversityBoard() {
		// 1. 유저 조회/검증
		UserDto user = apiUserResolver.getCurrentUserDto();
		userValidator.validateStudentVerification(user);

		// 2. 대학교 게시판 조회
		Board universityBoard = boardFinder.findUserUniversityBoard(user.universityId());

		return boardDtoMapper.mapToBoardWithFavoriteStatus(universityBoard, user);
	}

	public BoardWithFavoriteStatus getBoard(Long boardId) {
		// 유저 조회
		UserDto userDto = apiUserResolver.getCurrentUserDto();

		Board board = boardFinder.findBoard(boardId);

		return boardDtoMapper.mapToBoardWithFavoriteStatus(board, userDto);
	}

	public List<HomePostThumbnail> getTrendingPreview() {
		UserDto user = apiUserResolver.getCurrentUserDto();

		List<Long> trendingPostIds = trendingPostFinder.findAllPostIds();

		List<Post> trendingPosts = postFinder.findTopTrendingPosts(trendingPostIds, user.universityId());

		return postDtoMapper.toHomePostThumbnails(trendingPosts);
	}
}
