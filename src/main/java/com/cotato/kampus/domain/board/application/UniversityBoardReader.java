package com.cotato.kampus.domain.board.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.UniversityBoardRepository;
import com.cotato.kampus.domain.board.domain.UniversityBoard;
import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UniversityBoardReader {
	private final UniversityBoardRepository universityBoardRepository;


	public BoardDto read(Long universityId){
		UniversityBoard universityBoard = universityBoardRepository.findByUniversityId(universityId)
			.orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));

		return BoardDto.of(
			universityBoard.getId(),
			universityBoard.getBoardName());
	}
}
