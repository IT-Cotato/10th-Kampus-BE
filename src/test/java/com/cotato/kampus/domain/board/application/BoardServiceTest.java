package com.cotato.kampus.domain.board.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.domain.BoardFavorite;
import com.cotato.kampus.domain.board.domain.BoardWithFavoriteStatus;
import com.cotato.kampus.domain.board.domain.HomePostThumbnail;
import com.cotato.kampus.domain.board.domain.NormalBoard;
import com.cotato.kampus.domain.board.domain.UniversityBoard;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.board.implement.port.BoardFavoriteRepository;
import com.cotato.kampus.domain.board.implement.port.BoardRepository;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.helper.TestUserHelper;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class BoardServiceTest {
	@Autowired
	private BoardService boardService;

	@MockBean
	private ApiUserResolver apiUserResolver;

	@Autowired
	BoardRepository boardRepository;

	@Autowired
	private BoardFavoriteRepository boardFavoriteRepository;

	private Board savedBoard1;
	private Board savedBoard2;
	private Board savedBoard3;
	private Board savedBoard4;
	private Board savedBoard5;

	@BeforeEach
	void setUp() {
		savedBoard1 = boardRepository.save(NormalBoard.create("자유게시판", "자유게시판입니다", false,
			BoardStatus.ACTIVE, BoardType.NORMAL));
		savedBoard2 = boardRepository.save(NormalBoard.create("카드뉴스게시판", "카드뉴스게시판입니다", false,
			BoardStatus.ACTIVE, BoardType.CARDNEWS));
		savedBoard3 = boardRepository.save(NormalBoard.create("고정게시판", "고정게시판입니다", false,
			BoardStatus.ACTIVE, BoardType.FIXED));
		savedBoard4 = boardRepository.save(NormalBoard.create("트렌딩게시판", "트랜딩게시판입니다", false,
			BoardStatus.ACTIVE, BoardType.TRENDING));
		savedBoard5 = boardRepository.save(UniversityBoard.create("홍익대학교", "홍대생 전용 게시판입니다", false,
			BoardStatus.ACTIVE, BoardType.UNIVERSITY, 2L));
	}

	@Test
	@DisplayName("공용 게시판 목록 조회 테스트 - 성공")
	void getBoardList_success() {
		// Given
		Long userId = TestUserHelper.createUserDto(1L, null, UserRole.VERIFIED).id();
		given(apiUserResolver.getCurrentUserId()).willReturn(userId);

		// 트렌딩 게시판 즐겨찾기
		boardFavoriteRepository.save(BoardFavorite.builder()
			.userId(userId)
			.boardId(savedBoard4.getId())
			.build());

		// When
		List<BoardWithFavoriteStatus> result = boardService.getBoardList();

		// Then
		assertThat(result.size()).isEqualTo(4);
		// 즐겨찾기한 게시판 우선 정렬됨
		assertThat(result.get(0).isFavorite()).isEqualTo(true);
		assertThat(result.get(0).boardId()).isEqualTo(savedBoard4.getId());
	}

	@Test
	@DisplayName("즐겨찾는 게시판 미리보기 - 성공")
	void getBoardFavorite_success() {
		// Given
		Long userId = TestUserHelper.createUserDto(1L, null, UserRole.VERIFIED).id();
		given(apiUserResolver.getCurrentUserId()).willReturn(userId);

		// 고정 게시판 즐겨찾기
		boardFavoriteRepository.save(BoardFavorite.builder()
			.userId(userId)
			.boardId(savedBoard4.getId())
			.build());

		// When
		List<HomePostThumbnail> result = boardService.getFavoriteBoardPreview();

		// Then
		assertThat(result.size()).isEqualTo(1);
	}
}
