package com.cotato.kampus.global.auth.util;

import org.springframework.http.ResponseCookie;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

	private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
	private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 3;

	public static void setRefreshTokenCookie(HttpServletResponse response, final String refreshToken) {
		ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(COOKIE_MAX_AGE)
			.sameSite("None")
			.build();

		response.addHeader("Set-Cookie", cookie.toString());
	}

	public static String extractRefreshToken(HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		throw new AppException(ErrorCode.COOKIE_NOT_FOUND);
	}

	public static void clearRefreshTokenCookie(HttpServletResponse response) {
		ResponseCookie expiredCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(0)
			.sameSite("None")
			.build();

		response.addHeader("Set-Cookie", expiredCookie.toString());
	}
}