package com.cotato.kampus.global.auth.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.Cookie;

class CookieUtilTest {

	@Test
	@DisplayName("만료된 리프레시 쿠키 생성 검증")
	void createExpiredRefreshCookie_CreatesCorrectCookie() {
		// when
		Cookie expiredCookie = CookieUtil.createExpiredRefreshCookie();

		// then
		assertEquals("refreshToken", expiredCookie.getName());
		assertNull(expiredCookie.getValue());
		assertEquals(0, expiredCookie.getMaxAge());
		assertEquals("/", expiredCookie.getPath());
		assertTrue(expiredCookie.getSecure());
	}

}