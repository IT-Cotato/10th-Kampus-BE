package com.cotato.kampus.domain.board.application;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.dto.response.BoardResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BoardService {

	private final BoardReader boardReader;
	private final BoardFavoriteReader boardFavoriteReader;

	public List<BoardResponseDto.BoardDto> getBoardList(){

		// 즐겨찾는 게시판 조회
		Set<Long> favoriteBoardIds = boardFavoriteReader.read();

		// 전체 게시판 조회
		List<Board> boards = boardReader.read();

		// DTO 변환
		return BoardMapper.toBoardDtos(boards, favoriteBoardIds);
	}

}
