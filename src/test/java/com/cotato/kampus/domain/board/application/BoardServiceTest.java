package com.cotato.kampus.domain.board.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.dto.CategoryDto;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;


@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

	@Mock
	private BoardFinder boardFinder;

	@Mock
	private CategoryFinder categoryFinder;

	@InjectMocks
	private BoardService boardService;

	@Test
	@DisplayName("존재하는 게시판의 카테고리 목록을 조회")
	void findCategories() {
		// given
		Long boardId = 1L;

		// 테스트용 게시판 생성
		Board board = Board.builder()
			.boardName("테스트 게시판")
			.description("테스트 목적의 게시판입니다.")
			.universityId(null)
			.usesCategories(true)
			.boardStatus(BoardStatus.ACTIVE)
			.boardType(BoardType.GENERAL)
			.build();

		// 테스트용 카테고리 DTO 리스트 생성
		List<CategoryDto> expectedCategories = List.of(
			new CategoryDto(1L, "공지", boardId),
			new CategoryDto(2L, "질문", boardId),
			new CategoryDto(3L, "자유", boardId)
		);

		// BoardFinder가 해당 게시판을 찾을 수 있도록 설정
		when(boardFinder.findBoard(boardId)).thenReturn(board);

		// CategoryFinder가 카테고리 목록을 반환하도록 설정
		when(categoryFinder.findCategories(boardId)).thenReturn(expectedCategories);

		// when
		List<CategoryDto> result = boardService.findCategories(boardId);

		// then
		assertEquals(3, result.size());
		assertTrue(result.stream().anyMatch(cat -> "공지".equals(cat.categoryName())));
		assertTrue(result.stream().anyMatch(cat -> "질문".equals(cat.categoryName())));
		assertTrue(result.stream().anyMatch(cat -> "자유".equals(cat.categoryName())));

		// 메서드 호출 검증
		verify(boardFinder, times(1)).findBoard(boardId);
		verify(categoryFinder, times(1)).findCategories(boardId);
	}


	@Test
	@DisplayName("존재하지 않는 게시판의 ID로 조회 시 예외 발생")
	void findCategories_notFound() {
		// given
		Long nonExistentBoardId = 999L;

		// BoardFinder가 예외를 던지도록 설정
		when(boardFinder.findBoard(nonExistentBoardId))
			.thenThrow(new AppException(ErrorCode.BOARD_NOT_FOUND));

		// when & then
		AppException exception = assertThrows(
			AppException.class,
			() -> boardService.findCategories(nonExistentBoardId)
		);

		assertEquals(ErrorCode.BOARD_NOT_FOUND, exception.getErrorCode());

		// 예외가 발생하므로 categoryFinder는 호출되지 않아야 함
		verify(categoryFinder, never()).findCategories(anyLong());
	}

	@Test
	@DisplayName("카테고리가 없는 게시판의 카테고리 목록을 조회하면 빈 리스트를 반환")
	void findCategoies_empty() {
		// given
		Long boardId = 2L;

		// 카테고리가 없는 게시판 생성
		Board boardWithoutCategories = Board.builder()
			.boardName("카테고리 없는 게시판")
			.description("카테고리가 없는 테스트 게시판입니다.")
			.universityId(null)
			.usesCategories(false)
			.boardType(BoardType.GENERAL)
			.boardStatus(BoardStatus.ACTIVE)
			.build();

		// BoardFinder가 해당 게시판을 찾을 수 있도록 설정
		when(boardFinder.findBoard(boardId)).thenReturn(boardWithoutCategories);

		// CategoryFinder가 빈 목록을 반환하도록 설정
		when(categoryFinder.findCategories(boardId)).thenReturn(List.of());

		// when
		List<CategoryDto> result = boardService.findCategories(boardId);

		// then
		assertTrue(result.isEmpty());

		// 메서드 호출 검증
		verify(boardFinder, times(1)).findBoard(boardId);
		verify(categoryFinder, times(1)).findCategories(boardId);
	}
}