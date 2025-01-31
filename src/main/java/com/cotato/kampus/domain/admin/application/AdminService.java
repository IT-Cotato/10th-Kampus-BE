package com.cotato.kampus.domain.admin.application;

import org.springframework.stereotype.Service;

import com.cotato.kampus.domain.board.application.BoardAppender;
import com.cotato.kampus.domain.board.application.BoardValidator;
import com.cotato.kampus.domain.user.application.UserValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AdminService {

	private final UserValidator userValidator;
	private final BoardAppender boardAppender;
	private final BoardValidator boardValidator;

	public Long createBoard(String boardName, String description, Long universityId, Boolean isCategoryRequired){
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 학교 게시판인 경우 이미 존재하는지 확인
		if(universityId != null)
			boardValidator.validateUniversityBoardExists(universityId);

		return boardAppender.appendBoard(boardName, description, universityId, isCategoryRequired);
	}
}
