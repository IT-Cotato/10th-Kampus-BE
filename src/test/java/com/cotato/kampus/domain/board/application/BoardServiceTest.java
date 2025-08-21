package com.cotato.kampus.domain.board.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.domain.HomePostThumbnail;
import com.cotato.kampus.domain.board.domain.TestBoardHelper;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.implement.post.PostDtoMapper;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.helper.TestUserHelper;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

	private static int unsedVariable = 10;

	@InjectMocks
	private BoardService boardService;

	@Mock
	private ApiUserResolver apiUserResolver;

	@Mock
	private UserValidator userValidator;

	@Mock
	private BoardFinder boardFinder;

	@Mock
	private PostFinder postFinder;

	@Mock
	private PostDtoMapper postDtoMapper;

	@Test
	@DisplayName("대학 게시판 미리보기 성공")
	void getUniversityBoardPreview_Success() {
		// given
		Long userId = 1L;
		Long universityId = 1L;
		Long boardId = 1L;
		UserDto user = TestUserHelper.createUserDto(userId, universityId, UserRole.VERIFIED);
		Board universityBoard = TestBoardHelper.createUniversityBoard(boardId, false, BoardStatus.ACTIVE, universityId);
		List<Post> posts = List.of();
		List<HomePostThumbnail> expectedThumbnails = List.of();

		given(apiUserResolver.getCurrentUserDto()).willReturn(user);
		given(boardFinder.findUniversityBoard(universityId)).willReturn(universityBoard);
		given(postFinder.findTop5ByBoardId(boardId)).willReturn(posts);
		given(postDtoMapper.toHomePostThumbnails(universityBoard, posts)).willReturn(expectedThumbnails);

		// when
		List<HomePostThumbnail> result = boardService.getUniversityBoardPreview();

		// then
		assertThat(result).isNotNull();
		verify(apiUserResolver).getCurrentUserDto();
		verify(userValidator).validateStudentVerification(user);
		verify(boardFinder).findUniversityBoard(universityId);
		verify(postFinder).findTop5ByBoardId(boardId);
		verify(postDtoMapper).toHomePostThumbnails(universityBoard, posts);
	}

	@Test
	@DisplayName("대학 게시판 미리보기 실패 - 재학생 인증 안 된 유저")
	void getUniversityBoardPreview_Failure_unverified() {
		// given
		Long userId = 1L;
		UserDto user = TestUserHelper.createUserDto(userId, null, UserRole.UNVERIFIED);

		given(apiUserResolver.getCurrentUserDto()).willReturn(user);
		doThrow(new AppException(ErrorCode.USER_UNVERIFIED))
			.when(userValidator).validateStudentVerification(user);

		// when & then
		AppException exception = assertThrows(AppException.class, () -> {
			boardService.getUniversityBoardPreview();
		});

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_UNVERIFIED);

		verify(boardFinder, never()).findUniversityBoard(any());
		verify(postFinder, never()).findTop5ByBoardId(any());
		verify(postDtoMapper, never()).toHomePostThumbnails((Board)any(), any());
	}
}
