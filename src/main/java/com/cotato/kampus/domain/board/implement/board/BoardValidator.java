package com.cotato.kampus.domain.board.implement.board;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.domain.UniversityBoard;
import com.cotato.kampus.domain.board.implement.port.BoardRepository;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardValidator {
	private final BoardRepository boardRepository;

	public void validateBoardIsActive(Board board) {
		if (board.getBoardStatus() != BoardStatus.ACTIVE)
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

	public void validatePostCreationAccess(UserDto userDto, Board board) {
		// 대학 게시판인 경우 자격 검증
		if (board.getBoardType() == BoardType.UNIVERSITY) {
			validateUniversityBoardAccess(userDto, (UniversityBoard) board);
		} else if (board.getBoardType() == BoardType.CARDNEWS) {
			throw new AppException(ErrorCode.BOARD_ACCESS_DENIED);
		}
	}

	public void validateUniversityAccess(UserDto userDto, Board board) {
		// 대학 게시판인 경우 자격 검증
		if (board.getBoardType() == BoardType.UNIVERSITY) {
			validateUniversityBoardAccess(userDto, (UniversityBoard) board);
		}
	}

	public void isCategoryEnabled(Board board){
		if(!board.getUsesCategories()) {
			throw new AppException(ErrorCode.CATEGORY_NOT_ALLOWED);
		}
	}


	private void validateUniversityBoardAccess(UserDto userDto, UniversityBoard board) {
		if(userDto.userRole() == UserRole.UNVERIFIED) {
			throw new AppException(ErrorCode.BOARD_ACCESS_DENIED);
		}

		// 자기학교 게시판만 접근 가능
		if(!Objects.equals(board.getUniversityId(), userDto.universityId())) {
			throw new AppException(ErrorCode.BOARD_ACCESS_DENIED);
		}
	}

	public void validateBoardTypeAndUniversityId(BoardType boardType, Long universityId) {
		if(boardType != BoardType.UNIVERSITY && universityId != null) {
			throw new AppException(ErrorCode.INVALID_BOARD_TYPE);
		}
	}
}