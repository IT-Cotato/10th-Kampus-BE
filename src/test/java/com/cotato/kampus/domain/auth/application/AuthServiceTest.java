package com.cotato.kampus.domain.auth.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.auth.implement.RefreshManager;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.user.application.UserAppender;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.helper.TestUserHelper;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService 테스트")
class AuthServiceTest {

	@Mock
	private UserAppender userAppender;

	@Mock
	private RefreshManager refreshManager;

	@Mock
	private ApiUserResolver apiUserResolver;

	@InjectMocks
	private AuthService authService;

	@Test
	@DisplayName("로그아웃 시 리프레시 토큰 삭제 성공")
	void logout_Success() {
		// given
		UserDto userDto = TestUserHelper.createUserDto(1L, 1L, null);
		when(apiUserResolver.getCurrentUserDto()).thenReturn(userDto);

		// when
		assertDoesNotThrow(() -> authService.logout());

		// then
		verify(apiUserResolver).getCurrentUserDto();
		verify(refreshManager).deleteRefreshToken(userDto.uniqueId());
	}
}