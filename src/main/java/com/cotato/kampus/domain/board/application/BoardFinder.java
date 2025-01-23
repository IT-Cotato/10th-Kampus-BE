package com.cotato.kampus.domain.board.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.BoardRepository;
import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.dto.BoardWithFavoriteStatusDto;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFinder {
	private final BoardRepository boardRepository;

	public List<BoardWithFavoriteStatusDto> findAll(){
		return boardRepository.findAll().stream()
			.map(board -> BoardWithFavoriteStatusDto.of(
				board.getId(),
				board.getBoardName(),
				false
			))
			.toList();
	}

	public Board findBoard(Long boardId){
		return boardRepository.findById(boardId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));
	}
}