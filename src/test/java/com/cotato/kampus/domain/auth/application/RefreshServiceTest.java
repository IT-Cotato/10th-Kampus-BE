package com.cotato.kampus.domain.auth.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.auth.dao.RefreshRepository;
import com.cotato.kampus.domain.auth.domain.RefreshEntity;
import com.cotato.kampus.domain.auth.domain.ReissuedToken;
import com.cotato.kampus.domain.auth.factory.TokenTestDataFactory;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.JwtAuthenticationException;
import com.cotato.kampus.global.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
class RefreshServiceTest {

	@Mock
	private RefreshRepository refreshRepository;

	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private RefreshService refreshService;

	private String validRefreshToken;
	private String rawValidRefreshToken;
	private String testRefreshToken;

	@BeforeEach
	void setUp() {
		rawValidRefreshToken = TokenTestDataFactory.createRefreshToken();
		validRefreshToken = TokenTestDataFactory.createBearerRefreshToken();
		testRefreshToken = TokenTestDataFactory.createRefreshToken();
	}

	@Test
	@DisplayName("유효한 리프레시 토큰으로 요청 시 새로운 액세스 토큰과 리프레시 토큰을 발급하고 DB에 저장한다")
	void reissueRefreshToken_withValidToken_shouldReissueAndSaveTokens() {
		// given
		String newAccessToken = TokenTestDataFactory.createAccessToken();
		String newRefreshToken = TokenTestDataFactory.createRefreshToken();

		when(jwtUtil.getUniqueId(rawValidRefreshToken)).thenReturn(TokenTestDataFactory.TEST_UNIQUE_ID);
		when(jwtUtil.getUsername(rawValidRefreshToken)).thenReturn(TokenTestDataFactory.TEST_USERNAME);
		when(jwtUtil.getRole(rawValidRefreshToken)).thenReturn(TokenTestDataFactory.TEST_ROLE);
		when(jwtUtil.createJwt("access", TokenTestDataFactory.TEST_UNIQUE_ID, TokenTestDataFactory.TEST_USERNAME,
			TokenTestDataFactory.TEST_ROLE, TokenTestDataFactory.ACCESS_TOKEN_EXP))
			.thenReturn(newAccessToken);
		when(jwtUtil.createJwt("refresh", TokenTestDataFactory.TEST_UNIQUE_ID, TokenTestDataFactory.TEST_USERNAME,
			TokenTestDataFactory.TEST_ROLE, TokenTestDataFactory.REFRESH_TOKEN_EXP))
			.thenReturn(newRefreshToken);
		when(refreshRepository.existsByUniqueId(TokenTestDataFactory.TEST_UNIQUE_ID)).thenReturn(true);

		// when
		ReissuedToken reissuedToken = refreshService.reissueRefreshToken(validRefreshToken);

		// then
		assertNotNull(reissuedToken);
		assertEquals(TokenTestDataFactory.TOKEN_PREFIX + newAccessToken, reissuedToken.accessToken());
		assertEquals(TokenTestDataFactory.TOKEN_PREFIX + newRefreshToken, reissuedToken.refreshToken());

		verify(refreshRepository).existsByUniqueId(TokenTestDataFactory.TEST_UNIQUE_ID);
		verify(refreshRepository).deleteByUniqueId(TokenTestDataFactory.TEST_UNIQUE_ID);

		ArgumentCaptor<RefreshEntity> refreshEntityCaptor = ArgumentCaptor.forClass(RefreshEntity.class);
		verify(refreshRepository).save(refreshEntityCaptor.capture());
		RefreshEntity savedEntity = refreshEntityCaptor.getValue();

		assertEquals(TokenTestDataFactory.TEST_UNIQUE_ID, savedEntity.getUniqueId());
		assertEquals(TokenTestDataFactory.TEST_USERNAME, savedEntity.getUsername());
		assertEquals(newRefreshToken, savedEntity.getRefresh());
		assertNotNull(savedEntity.getExpiration());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {"InvalidToken", "BearerInvalid", "Bearer"})
	@DisplayName("유효하지 않은 형식의 리프레시 토큰으로 요청 시 JwtAuthenticationException 발생")
	void reissueRefreshToken_withInvalidTokenFormat_shouldThrowJwtAuthenticationException(String invalidToken) {
		// when & then
		JwtAuthenticationException exception = assertThrows(JwtAuthenticationException.class, () -> {
			refreshService.reissueRefreshToken(invalidToken);
		});
		assertEquals(ErrorCode.INVALID_TOKEN, exception.getErrorCode());
	}

	@Test
	@DisplayName("기존에 해당 uniqueId의 리프레시 토큰이 존재하면 삭제 후 새로 저장한다")
	void addRefreshEntity_whenTokenExists_shouldDeleteAndSave() {
		// given
		when(refreshRepository.existsByUniqueId(TokenTestDataFactory.TEST_UNIQUE_ID)).thenReturn(true);

		// when
		refreshService.addRefreshEntity(TokenTestDataFactory.TEST_UNIQUE_ID, TokenTestDataFactory.TEST_USERNAME,
			testRefreshToken, TokenTestDataFactory.REFRESH_TOKEN_EXP);

		// then
		verify(refreshRepository).deleteByUniqueId(TokenTestDataFactory.TEST_UNIQUE_ID);

		ArgumentCaptor<RefreshEntity> refreshEntityCaptor = ArgumentCaptor.forClass(RefreshEntity.class);
		verify(refreshRepository).save(refreshEntityCaptor.capture());
		RefreshEntity savedEntity = refreshEntityCaptor.getValue();

		assertEquals(TokenTestDataFactory.TEST_UNIQUE_ID, savedEntity.getUniqueId());
		assertEquals(TokenTestDataFactory.TEST_USERNAME, savedEntity.getUsername());
		assertEquals(testRefreshToken, savedEntity.getRefresh());
		assertNotNull(savedEntity.getExpiration());
	}

	@Test
	@DisplayName("기존에 해당 uniqueId의 리프레시 토큰이 없으면 바로 새로 저장한다")
	void addRefreshEntity_whenTokenNotExists_shouldSave() {
		// given
		when(refreshRepository.existsByUniqueId(TokenTestDataFactory.TEST_UNIQUE_ID)).thenReturn(false);

		// when
		refreshService.addRefreshEntity(TokenTestDataFactory.TEST_UNIQUE_ID, TokenTestDataFactory.TEST_USERNAME,
			testRefreshToken, TokenTestDataFactory.REFRESH_TOKEN_EXP);

		// then
		verify(refreshRepository, never()).deleteByUniqueId(TokenTestDataFactory.TEST_UNIQUE_ID);

		ArgumentCaptor<RefreshEntity> refreshEntityCaptor = ArgumentCaptor.forClass(RefreshEntity.class);
		verify(refreshRepository).save(refreshEntityCaptor.capture());
		RefreshEntity savedEntity = refreshEntityCaptor.getValue();

		assertEquals(TokenTestDataFactory.TEST_UNIQUE_ID, savedEntity.getUniqueId());
		assertEquals(TokenTestDataFactory.TEST_USERNAME, savedEntity.getUsername());
		assertEquals(testRefreshToken, savedEntity.getRefresh());
		assertNotNull(savedEntity.getExpiration());
	}
}