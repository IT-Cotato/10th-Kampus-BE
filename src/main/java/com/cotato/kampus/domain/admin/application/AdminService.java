package com.cotato.kampus.domain.admin.application;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.admin.dto.BoardDetail;
import com.cotato.kampus.domain.admin.dto.StudentVerification;
import com.cotato.kampus.domain.board.application.BoardAppender;
import com.cotato.kampus.domain.board.application.BoardFinder;
import com.cotato.kampus.domain.board.application.BoardUpdater;
import com.cotato.kampus.domain.board.application.BoardValidator;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.application.VerificationRecordFinder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AdminService {

	private final UserValidator userValidator;
	private final BoardAppender boardAppender;
	private final BoardUpdater boardUpdater;
	private final BoardValidator boardValidator;
	private final BoardFinder boardFinder;
	private final VerificationRecordFinder verificationRecordFinder;

	@Transactional
	public Long createBoard(String boardName, String description, Long universityId, Boolean isCategoryRequired){
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 학교 게시판인 경우 이미 존재하는지 확인
		if(universityId != null)
			boardValidator.validateUniversityBoardExists(universityId);

		return boardAppender.appendBoard(boardName, description, universityId, isCategoryRequired);
	}

	@Transactional
	public void updateBoard(Long boardId, String boardName, String description, Boolean isCategoryRequired){
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 게시판 업데이트
		boardUpdater.update(boardId, boardName, description, isCategoryRequired);
	}

	@Transactional
	public void inactiveBoard(Long boardId){
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 게시판 비활성화
		boardUpdater.inactiveBoard(boardId);
	}

	@Transactional
	public void activeBoard(Long boardId){
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 게시판 활성화
		boardUpdater.activeBoard(boardId);
	}

	@Transactional
	public List<BoardDetail> getAllBoards(){
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 각 게시판의 게시글 수 매핑하여 반환
		return boardFinder.findAllBoards();
	}

	@Transactional
	public Slice<StudentVerification> getVerifications(int page){
		// 관리자 검증
		userValidator.validateAdminAccess();

		return verificationRecordFinder.findAll(page);
	}
}
