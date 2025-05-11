package com.cotato.kampus.domain.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.user.dao.UserRepository;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.domain.user.enums.UserStatus;

@ExtendWith(MockitoExtension.class)
class UserUpdaterTest {

	@Mock
	private ApiUserResolver apiUserResolver;

	@Mock
	private UserFinder userFinder;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserUpdater userUpdater;

	/**
	 * 유저 객체에 ID를 설정하는 헬퍼 메소드
	 */
	private User setUserId(User user, Long id) {
		try {
			Field idField = User.class.getDeclaredField("id");
			idField.setAccessible(true);
			idField.set(user, id);
			return user;
		} catch (Exception e) {
			throw new RuntimeException("유저 ID 설정 중 오류 발생", e);
		}
	}

	@Test
	@DisplayName("이미 VERIFIED 유저가 대학 변경 시 ROLE은 유지되고 대학만 변경됨")
	void updateVerificationStatus_AlreadyVerified_UpdatesUniversityOnly() {
		// Given
		Long userId = 1L;
		Long oldUniversityId = 100L;
		Long newUniversityId = 200L;

		// VERIFIED 유저 생성
		User verifiedUser = User.builder()
			.email("verified@example.com")
			.uniqueId("verified-unique-id")
			.providerId("verified-provider-id")
			.username("verifiedUser")
			.nickname("verifiedNickname")
			.nationality(Nationality.KOREA)
			.preferredLanguage(PreferredLanguage.KOREAN)
			.userRole(UserRole.VERIFIED)
			.userStatus(UserStatus.ACTIVE)
			.build();
		verifiedUser.setUniversityId(oldUniversityId);
		setUserId(verifiedUser, userId);

		// When
		when(userFinder.findById(userId)).thenReturn(verifiedUser);

		Long resultId = userUpdater.updateVerificationStatus(userId, newUniversityId);

		// Then
		assertThat(resultId).isEqualTo(userId);
		assertThat(verifiedUser.getUserRole()).isEqualTo(UserRole.VERIFIED); // ROLE 변경 없음
		assertThat(verifiedUser.getUniversityId()).isEqualTo(newUniversityId); // 대학만 변경됨

		verify(userFinder).findById(userId);
	}

	@Test
	@DisplayName("UNVERIFIED 유저가 대학 인증 시 ROLE이 VERIFIED로 변경되고 대학이 설정됨")
	void updateVerificationStatus_Unverified_UpdatesRoleAndUniversity() {
		// Given
		Long userId = 2L;
		Long universityId = 300L;

		// UNVERIFIED 유저 생성
		User unverifiedUser = User.builder()
			.email("test@example.com")
			.uniqueId("test-unique-id")
			.providerId("test-provider-id")
			.username("testUser")
			.nickname("testNickname")
			.nationality(Nationality.KOREA)
			.preferredLanguage(PreferredLanguage.KOREAN)
			.userRole(UserRole.UNVERIFIED)
			.userStatus(UserStatus.ACTIVE)
			.build();
		setUserId(unverifiedUser, userId);

		// When
		when(userFinder.findById(userId)).thenReturn(unverifiedUser);

		Long resultId = userUpdater.updateVerificationStatus(userId, universityId);

		// Then
		assertThat(resultId).isEqualTo(userId);
		assertThat(unverifiedUser.getUserRole()).isEqualTo(UserRole.VERIFIED); // UNVERIFIED -> VERIFIED로 변경
		assertThat(unverifiedUser.getUniversityId()).isEqualTo(universityId); // 대학 설정됨

		verify(userFinder).findById(userId);
	}

	@Test
	@DisplayName("ADMIN 유저는 ROLE 유지하고 대학만 변경됨")
	void updateVerificationStatus_Admin_UpdatesUniversityOnly() {
		// Given
		Long userId = 3L;
		Long oldUniversityId = 400L;
		Long newUniversityId = 500L;

		// ADMIN 유저 생성
		User adminUser = User.builder()
			.email("admin@example.com")
			.uniqueId("admin-unique-id")
			.providerId("admin-provider-id")
			.username("adminUser")
			.nickname("adminNickname")
			.nationality(Nationality.KOREA)
			.preferredLanguage(PreferredLanguage.KOREAN)
			.userRole(UserRole.ADMIN)
			.userStatus(UserStatus.ACTIVE)
			.build();
		adminUser.setUniversityId(oldUniversityId);
		setUserId(adminUser, userId);

		// When
		when(userFinder.findById(userId)).thenReturn(adminUser);

		Long resultId = userUpdater.updateVerificationStatus(userId, newUniversityId);

		// Then
		assertThat(resultId).isEqualTo(userId);
		assertThat(adminUser.getUserRole()).isEqualTo(UserRole.ADMIN); // ROLE 변경 없음
		assertThat(adminUser.getUniversityId()).isEqualTo(newUniversityId); // 대학만 변경됨

		verify(userFinder).findById(userId);
	}
} 