package com.cotato.kampus.domain.board.application;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.BoardRepository;
import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardValidator {
	private final BoardRepository boardRepository;

	public void validateBoardIsActive(BoardDto boardDto) {
		if (boardDto.boardStatus() != BoardStatus.ACTIVE)
			throw new AppException(ErrorCode.BOARD_NOT_VALIDATE);
	}

	public void validateUniversityBoardExists(Long universityId) {
		if (boardRepository.existsByUniversityId(universityId)) {
			throw new AppException(ErrorCode.UNIVERSITY_BOARD_DUPLICATED);
		}
	}

	public void validateUniqueName(String boardName) {
		if (boardRepository.existsByBoardName(boardName)) {
			throw new AppException(ErrorCode.BOARD_NAME_DUPLICATED);
		}
	}

	public void validateBoardAccess(UserDto userDto, BoardDto boardDto) {
		// 학교 게시판인 경우 자격 검증
		if (boardDto.boardType() == BoardType.UNIVERSITY &&
			!Objects.equals(boardDto.universityId(), userDto.universityId())) {
			throw new AppException(ErrorCode.BOARD_ACCESS_DENIED);
		}

		// 카드뉴스 게시판 접근 불가
		if (boardDto.boardType() == BoardType.CARDNEWS)
			throw new AppException(ErrorCode.BOARD_ACCESS_DENIED);
	}
}