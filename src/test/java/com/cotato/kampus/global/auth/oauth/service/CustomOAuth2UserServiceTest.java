package com.cotato.kampus.global.auth.oauth.service;

import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.board.implement.boardFavorite.BoardFavoriteManager;
import com.cotato.kampus.domain.user.dao.UserRepository;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.global.auth.oauth.service.dto.OAuth2Attribute;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BoardFinder boardFinder;

    @Mock
    private BoardFavoriteManager boardFavoriteManager;

    @DisplayName("신규 유저일 경우, 기본 즐겨찾기 게시판을 추가한다")
    @Test
    void saveOrUpdate_newUser_addDefaultFavorites() {
        // given
        String uniqueId = "uniqueId";
        Long fakeUserId = 1L; // 테스트용 가짜 ID
        OAuth2Attribute attribute = OAuth2Attribute.builder()
                .username("testuser")
                .email("test@test.com")
                .providerId("providerId")
                .attributes(Map.of())
                .build();
        List<Long> defaultBoardIds = List.of(1L, 2L);

        // save 메소드가 반환할 User 모의 객체 생성
        User savedUser = mock(User.class);

        // 모의 객체의 getId()가 가짜 ID를 반환하도록 설정
        given(savedUser.getId()).willReturn(fakeUserId);

        // userRepository.save()가 호출되면 위에서 만든 모의 객체를 반환하도록 설정
        given(userRepository.findByUniqueId(uniqueId)).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willReturn(savedUser);
        given(boardFinder.findDefaultFavoriteBoardIds()).willReturn(defaultBoardIds);

        // when
        customOAuth2UserService.saveOrUpdate(attribute, uniqueId);

        // then
        // appendAll이 가짜 ID로 올바르게 호출되었는지 검증
        verify(boardFinder, times(1)).findDefaultFavoriteBoardIds();
        verify(boardFavoriteManager, times(1)).appendAll(eq(fakeUserId), eq(defaultBoardIds));
    }

    @DisplayName("기존 유저일 경우, 기본 즐겨찾기를 추가하지 않는다")
    @Test
    void saveOrUpdate_existingUser() {
        // given
        String uniqueId = "uniqueId";
        OAuth2Attribute attribute = OAuth2Attribute.builder()
                .username("testuser")
                .email("test@test.com")
                .providerId("providerId")
                .attributes(Map.of())
                .build();

        User existingUser = User.builder().build();

        given(userRepository.findByUniqueId(uniqueId)).willReturn(Optional.of(existingUser)); // 기존 사용자이므로 Optional.of(existingUser)를 반환하도록 설정
        given(userRepository.save(any(User.class))).willReturn(existingUser);

        // when
        customOAuth2UserService.saveOrUpdate(attribute, uniqueId);

        // then
        verify(boardFinder, never()).findDefaultFavoriteBoardIds();
        verify(boardFavoriteManager, never()).appendAll(anyLong(), anyList());
    }
}
