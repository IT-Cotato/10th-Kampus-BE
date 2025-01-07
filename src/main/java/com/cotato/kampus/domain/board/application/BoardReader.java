package com.cotato.kampus.domain.board.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.BoardRepository;
import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardReader {
	private final BoardRepository boardRepository;

	public List<BoardDto> readAll(){
		return boardRepository.findAll().stream()
			.map(board -> BoardDto.of(
				board.getId(),
				board.getBoardName(),
				false
			))
			.toList();
	}

	public void validateBoardExists(Long boardId){
		if(!boardRepository.existsById(boardId))
			throw new AppException(ErrorCode.BOARD_NOT_FOUND);
	}
}