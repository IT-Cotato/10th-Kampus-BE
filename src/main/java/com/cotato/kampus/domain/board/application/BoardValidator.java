package com.cotato.kampus.domain.board.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.BoardRepository;
import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardValidator {
	private final BoardRepository boardRepository;
	private final BoardFinder boardFinder;

	public void validateBoardExistsAndActive(Long boardId){
		if(!boardRepository.existsById(boardId))
			throw new AppException(ErrorCode.BOARD_NOT_FOUND);

		BoardDto boardDto = boardFinder.findBoard(boardId);
		if(boardDto.boardStatus()!= BoardStatus.ACTIVE)
			throw new AppException(ErrorCode.BOARD_NOT_VALIDATE);
	}

	public void validateUniversityBoardExists(Long universityId){
		if(boardRepository.existsByUniversityId(universityId)){
			throw new AppException(ErrorCode.UNIVERSITY_BOARD_DUPLICATED);
		}
	}
}