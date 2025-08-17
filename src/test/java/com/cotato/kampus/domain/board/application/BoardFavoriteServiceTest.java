package com.cotato.kampus.domain.board.application;

import com.cotato.kampus.domain.board.domain.BoardFavorite;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.board.implement.boardFavorite.BoardFavoriteFinder;
import com.cotato.kampus.domain.board.implement.boardFavorite.BoardFavoriteManager;
import com.cotato.kampus.domain.board.implement.boardFavorite.BoardFavoriteValidator;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.helper.TestUserHelper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class BoardFavoriteServiceTest {

    @InjectMocks
    private BoardFavoriteService boardFavoriteService;

    @Mock
    private ApiUserResolver apiUserResolver;

    @Mock
    private BoardFinder boardFinder;

    @Mock
    private BoardFavoriteValidator boardFavoriteValidator;

    @Mock
    private BoardFavoriteFinder boardFavoriteFinder;

    @Mock
    private BoardFavoriteManager boardFavoriteManager;

    private final UserDto user = TestUserHelper.createUserDto(1L, 401L, UserRole.VERIFIED);
    private final Long boardId = 1L;

    @Test
    @DisplayName("게시판 즐겨찾기 해제 성공")
    void removeFavoriteBoard_Success() {
        // given
        BoardFavorite boardFavorite = BoardFavorite.builder().build();
        given(apiUserResolver.getCurrentUserDto()).willReturn(user);
        given(boardFinder.findBoardType(boardId)).willReturn(BoardType.NORMAL);
        given(boardFavoriteFinder.findByUserIdAndBoardId(user.id(), boardId)).willReturn(boardFavorite);
        willDoNothing().given(boardFavoriteValidator).validateRemovable(BoardType.NORMAL);
        willDoNothing().given(boardFavoriteManager).deleteFavoriteBoard(boardFavorite);

        // when
        Long removedBoardId = boardFavoriteService.removeFavoriteBoard(boardId);

        // then
        assertThat(removedBoardId).isEqualTo(boardId);
        verify(apiUserResolver, times(1)).getCurrentUserDto();
        verify(boardFinder, times(1)).findBoardType(boardId);
        verify(boardFavoriteValidator, times(1)).validateRemovable(BoardType.NORMAL);
        verify(boardFavoriteFinder, times(1)).findByUserIdAndBoardId(user.id(), boardId);
        verify(boardFavoriteManager, times(1)).deleteFavoriteBoard(boardFavorite);
    }

    @Test
    @DisplayName("고정 게시판은 즐겨찾기 해제할 수 없어 실패한다")
    void removeFavoriteBoard_Fail_CannotRemove() {
        // given
        given(apiUserResolver.getCurrentUserDto()).willReturn(user);
        given(boardFinder.findBoardType(boardId)).willReturn(BoardType.FIXED);
        given(boardFavoriteFinder.findByUserIdAndBoardId(user.id(), boardId)).willReturn(BoardFavorite.builder().build());
        doThrow(new AppException(ErrorCode.CANNOT_REMOVE_FAVORITE_BOARD))
                .when(boardFavoriteValidator).validateRemovable(eq(BoardType.FIXED));

        // when & then
        AppException exception = assertThrows(AppException.class,
                () -> boardFavoriteService.removeFavoriteBoard(boardId));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CANNOT_REMOVE_FAVORITE_BOARD);
        verify(apiUserResolver, times(1)).getCurrentUserDto();
        verify(boardFinder, times(1)).findBoardType(boardId);
        verify(boardFavoriteValidator, times(1)).validateRemovable(BoardType.FIXED);
        verify(boardFavoriteFinder, times(1)).findByUserIdAndBoardId(user.id(), boardId);
        verify(boardFavoriteManager, never()).deleteFavoriteBoard(any(BoardFavorite.class));
    }
}
