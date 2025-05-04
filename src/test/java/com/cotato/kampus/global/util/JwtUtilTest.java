package com.cotato.kampus.global.util;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.JwtException;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

	private JwtUtil jwtUtil;
	private final String SECRET_KEY = "testSecretKeyForJwtUtilTestMustBeAtLeast32BytesLong";
	private final String CATEGORY = "test";
	private final String UNIQUE_ID = "testUniqueId";
	private final String USERNAME = "testUser";
	private final String ROLE = "USER";

	@BeforeEach
	void setUp() {
		jwtUtil = new JwtUtil(SECRET_KEY);
	}

	@Test
	@DisplayName("토큰 검증 성공: 유효한 토큰이 주어졌을 때 예외 없이 처리되어야 한다")
	void isExpired_withValidToken_shouldReturnFalse() {
		// given
		long validExpirationMs = 3600000L; // 1시간
		String validToken = jwtUtil.createJwt(CATEGORY, UNIQUE_ID, USERNAME, ROLE, validExpirationMs);

		// when
		// then
		Assertions.assertThatCode(() -> jwtUtil.validateToken(validToken))
			.doesNotThrowAnyException();

	}

	@Test
	@DisplayName("토큰 검증 실패: 만료된 토큰이 주어졌을 때 예외 발생")
	void isExpired_withExpiredToken_shouldReturnTrue() {
		// given
		long expiredExpirationMs = -1000L; // 만료된 토큰 생성을 위해 음수값 사용
		String expiredToken = jwtUtil.createJwt(CATEGORY, UNIQUE_ID, USERNAME, ROLE, expiredExpirationMs);

		// when
		assertThatThrownBy(() -> jwtUtil.validateToken(expiredToken))
			.isInstanceOf(JwtException.class)
			.hasMessage(ErrorCode.TOKEN_EXPIRED.getMessage());
	}

	@Test
	@DisplayName("토큰 검증 실패: 잘못된 형식의 토큰이 주어졌을 때 예외 발생")
	void isExpired_withInvalidToken_shouldHandleExceptionAndReturnTrue() {
		// given
		String invalidToken = "invalid.jwt.token";

		// when
		// then
		assertThatThrownBy(() -> jwtUtil.validateToken(invalidToken))
			.isInstanceOf(JwtException.class)
			.hasMessage(ErrorCode.MALFORMED_TOKEN.getMessage());
	}
}