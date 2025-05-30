package com.cotato.kampus.domain.auth.factory;

// 테스트용 토큰 데이터를 생성하는 Factory 클래스
public class TokenTestDataFactory {

	public static final String TOKEN_PREFIX = "Bearer ";
	public static final Long ACCESS_TOKEN_EXP = 604800000L;
	public static final Long REFRESH_TOKEN_EXP = 86400000L;

	// 테스트 사용자 정보
	public static final String TEST_UNIQUE_ID = "testUniqueId";
	public static final String TEST_USERNAME = "testUser";
	public static final String TEST_ROLE = "ROLE_USER";

	// 액세스 토큰 생성
	public static String createAccessToken() {
		return "test-access-token";
	}

	// 리프레시 토큰 생성
	public static String createRefreshToken() {
		return "test-refresh-token";
	}

	// Bearer 형식의 액세스 토큰 생성
	public static String createBearerAccessToken() {
		return TOKEN_PREFIX + createAccessToken();
	}

	// Bearer 형식의 리프레시 토큰 생성
	public static String createBearerRefreshToken() {
		return TOKEN_PREFIX + createRefreshToken();
	}
}