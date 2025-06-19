package com.cotato.kampus.domain.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.auth.dao.RefreshRepository;
import com.cotato.kampus.domain.auth.domain.ReissuedToken;
import com.cotato.kampus.domain.auth.factory.TokenTestDataFactory;
import com.cotato.kampus.domain.auth.implement.RefreshManager;
import com.cotato.kampus.global.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshService 테스트")
class RefreshServiceTest {

	@InjectMocks
	private RefreshService refreshService;

	@Mock
	private RefreshRepository refreshRepository;

	@Mock
	private RefreshManager refreshManager;

	@Mock
	private JwtUtil jwtUtil;

	@Test
	@DisplayName("리프레시 토큰 재발급 - 성공")
	void reissueRefreshToken_Success() {
		// given
		String inputRefreshToken = TokenTestDataFactory.createRefreshToken();
		String expectedNewAccessToken = "new-access-token";
		String expectedNewRefreshToken = "new-refresh-token";

		// JwtUtil 모킹
		when(jwtUtil.getUniqueId(inputRefreshToken)).thenReturn(TokenTestDataFactory.TEST_UNIQUE_ID);
		when(jwtUtil.getUsername(inputRefreshToken)).thenReturn(TokenTestDataFactory.TEST_USERNAME);
		when(jwtUtil.getRole(inputRefreshToken)).thenReturn(TokenTestDataFactory.TEST_ROLE);

		when(jwtUtil.createJwt(
			eq("access"),
			eq(TokenTestDataFactory.TEST_UNIQUE_ID),
			eq(TokenTestDataFactory.TEST_USERNAME),
			eq(TokenTestDataFactory.TEST_ROLE),
			eq(604800000L)
		)).thenReturn(expectedNewAccessToken);

		when(jwtUtil.createJwt(
			eq("refresh"),
			eq(TokenTestDataFactory.TEST_UNIQUE_ID),
			eq(TokenTestDataFactory.TEST_USERNAME),
			eq(TokenTestDataFactory.TEST_ROLE),
			eq(86400000L)
		)).thenReturn(expectedNewRefreshToken);

		// when
		ReissuedToken result = refreshService.reissueRefreshToken(inputRefreshToken);

		// then
		assertThat(result).isNotNull();
		assertThat(result.accessToken()).isEqualTo("Bearer " + expectedNewAccessToken);
		assertThat(result.refreshToken()).isEqualTo(expectedNewRefreshToken);
	}
}