package com.cotato.kampus.domain.board.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.BoardRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardValidator {
	private final BoardRepository boardRepository;


	public void validateBoardExists(Long boardId){
		if(!boardRepository.existsById(boardId))
			throw new AppException(ErrorCode.BOARD_NOT_FOUND);
	}

}
