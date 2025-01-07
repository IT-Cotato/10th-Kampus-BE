package com.cotato.kampus.domain.board.application;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cotato.kampus.domain.board.dto.BoardDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BoardService {

	private final BoardReader boardReader;
	private final BoardFavoriteReader boardFavoriteReader;

	public List<BoardDto> getBoardList(){

		// 즐겨찾는 게시판 조회
		Set<Long> favoriteBoardIds = boardFavoriteReader.read();

		// 전체 게시판 조회
		List<BoardDto> boards = boardReader.read();

		// 즐겨찾기 여부 매핑
		return BoardDtoEnhancer.updateFavoriteStatus(boards, favoriteBoardIds);
	}

	public List<BoardDto> getFavoriteBoardList() {
		// 즐겨찾는 게시판 조회
		Set<Long> favoriteBoardIds = boardFavoriteReader.read();

		// 전체 게시판 조회
		List<BoardDto> boards = boardReader.read();

		// 즐겨찾는 게시판만 필터링
		return BoardDtoEnhancer.filterFavoriteBoards(boards, favoriteBoardIds);
	}

}
