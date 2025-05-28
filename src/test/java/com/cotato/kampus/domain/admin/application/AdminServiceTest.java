package com.cotato.kampus.domain.admin.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.NormalBoard;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.board.implement.port.BoardRepository;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class AdminServiceTest {

	@Autowired
	private AdminService adminService;

	@MockBean
	private UserValidator userValidator;

	@Autowired
	private BoardRepository boardRepository;

	@Test
	@DisplayName("게시판 생성 테스트 - CARDNEWS 타입 중복 생성 예외")
	void createBoard_duplicateUniqueType_CARDNEWS() {
		// Given
		doNothing().when(userValidator).validateAdminAccess();
		boardRepository.save(NormalBoard.create("카드뉴스 게시판1", "카드뉴스 게시판입니다,", false, BoardStatus.ACTIVE, BoardType.CARDNEWS));

		// When&Then
		assertThatThrownBy(() -> adminService.createBoard("카드뉴스 게시판2", "두 번쨰 카드뉴스 게시판입니다.", BoardType.CARDNEWS, null, List.of()))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.DUPLICATED_UNIQUE_BOARD_TYPE.getMessage());
	}

	@Test
	@DisplayName("게시판 생성 테스트 - TRENDING 타입 중복 생성 예외")
	void createBoard_duplicateUniqueType_TRENDING() {
		// Given
		doNothing().when(userValidator).validateAdminAccess();
		boardRepository.save(NormalBoard.create("트렌딩 게시판1", "트렌딩 게시판입니다,", false, BoardStatus.ACTIVE, BoardType.TRENDING));

		// When&Then
		assertThatThrownBy(() -> adminService.createBoard("트렌딩 게시판2", "두 번쨰 트렌딩 게시판입니다.", BoardType.TRENDING, null, List.of()))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.DUPLICATED_UNIQUE_BOARD_TYPE.getMessage());
	}
}