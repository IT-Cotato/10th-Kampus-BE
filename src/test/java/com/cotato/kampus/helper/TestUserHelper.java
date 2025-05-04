package com.cotato.kampus.helper;

import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.domain.user.enums.UserStatus;

public class TestUserHelper {

	// 기본값 상수
	// private static final Long DEFAULT_ID = 1L;
	private static final String DEFAULT_EMAIL = "test@kampus.com";
	private static final String DEFAULT_UNIQUE_ID = "test-unique";
	private static final String DEFAULT_PROVIDER_ID = "provider-1";
	private static final String DEFAULT_USERNAME = "테스트유저";
	private static final String DEFAULT_NICKNAME = "테스트닉네임";
	// private static final Long DEFAULT_UNIVERSITY_ID = 1L;
	private static final String DEFAULT_PROFILE_IMAGE = "https://example.com/profile.jpg";
	private static final Nationality DEFAULT_NATIONALITY = Nationality.KOREA;
	private static final PreferredLanguage DEFAULT_LANGUAGE = PreferredLanguage.KOREAN;
	private static final String DEFAULT_DEVICE_TOKEN = "device-token";
	// private static final UserRole DEFAULT_ROLE = UserRole.VERIFIED;
	private static final UserStatus DEFAULT_STATUS = UserStatus.ACTIVE;

	public static UserDto createUserDto(
		Long id,
		Long universityId,
		UserRole role
	) {
		return new UserDto(
			id,                           // id
			DEFAULT_EMAIL,                       // email
			DEFAULT_UNIQUE_ID,           // uniqueId
			DEFAULT_PROVIDER_ID,         // providerId
			DEFAULT_USERNAME,            // username
			DEFAULT_NICKNAME,            // nickname
			universityId,       // universityId
			DEFAULT_PROFILE_IMAGE,       // profileImage
			DEFAULT_NATIONALITY,         // nationality
			DEFAULT_LANGUAGE,            // preferredLanguage
			DEFAULT_DEVICE_TOKEN,        // deviceToken
			role,                        // userRole
			DEFAULT_STATUS                       // userStatus
		);
	}
}
