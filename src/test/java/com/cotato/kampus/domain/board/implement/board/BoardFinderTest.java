package com.cotato.kampus.domain.board.implement.board;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.board.implement.port.BoardRepository;

@ExtendWith(MockitoExtension.class)
class BoardFinderTest {

	@InjectMocks
	private BoardFinder boardFinder;

	@Mock
	private BoardRepository boardRepository;

	@Test
	@DisplayName("기본 즐겨찾기 게시판 ID 조회 시, FIXED, CARDNEWS, TRENDING 타입을 모두 포함하여 조회한다.")
	void findDefaultFavoriteBoardIds_Success() {
		// given
		List<BoardType> expectedTypes = List.of(BoardType.FIXED, BoardType.CARDNEWS, BoardType.TRENDING);
		List<Long> expectedBoardIds = List.of(1L, 2L, 3L);

		given(boardRepository.findBoardIdsByBoardTypeIn(expectedTypes)).willReturn(expectedBoardIds);

		// when
		List<Long> actualBoardIds = boardFinder.findDefaultFavoriteBoardIds();

		// then
		verify(boardRepository, times(1)).findBoardIdsByBoardTypeIn(expectedTypes);
		assertThat(actualBoardIds).isEqualTo(expectedBoardIds);
	}
}