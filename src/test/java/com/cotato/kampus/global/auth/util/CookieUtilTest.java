package com.cotato.kampus.global.auth.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

class CookieUtilTest {

	@Test
	@DisplayName("만료된 리프레시 쿠키 생성 검증")
	void clearRefreshTokenCookie_CreatesCorrectExpiredCookie() {
		// given
		MockHttpServletResponse response = new MockHttpServletResponse();

		// when
		CookieUtil.clearRefreshTokenCookie(response);

		// then
		String setCookieHeader = response.getHeader("Set-Cookie");
		assertNotNull(setCookieHeader);
		
		assertTrue(setCookieHeader.contains("refreshToken="));
		assertTrue(setCookieHeader.contains("Max-Age=0"));
		assertTrue(setCookieHeader.contains("Path=/"));
		assertTrue(setCookieHeader.contains("Secure"));
		assertTrue(setCookieHeader.contains("HttpOnly"));
		assertTrue(setCookieHeader.contains("SameSite=None"));
	}

}