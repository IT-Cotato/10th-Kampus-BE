package com.cotato.kampus.domain.user.application;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.university.application.UnivFinder;
import com.cotato.kampus.domain.university.domain.University;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.dto.UserDetailsDto;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.domain.user.enums.UserStatus;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private ApiUserResolver apiUserResolver;
	@Mock
	private UnivFinder univFinder;

	@InjectMocks
	private UserService userService;


	@Test
	public void UserService_GetUserDetails_ReturnUserDetailsDto_Unverified() {
		// Arrange
		// 대학교 ID를 가지고 있지 않은 경우의 UserDto 생성
		User user = User.builder()
			.email("e1")
			.uniqueId("u1")
			.providerId("p1")
			.username("u1")
			.nickname("n1")
			.nationality(Nationality.KOREA)
			.preferredLanguage(PreferredLanguage.KOREAN)
			.userRole(UserRole.UNVERIFIED)
			.userStatus(UserStatus.ACTIVE)
			.build();
		UserDto userDto = UserDto.from(user);

		// UserDetailsDto 생성 (예상 결과)
		UserDetailsDto expectedUserDetailsDto = UserDetailsDto.of(userDto, -1L, "");

		// Mock 설정
		when(apiUserResolver.getCurrentUserDto()).thenReturn(userDto);

		// Act
		UserDetailsDto result = userService.getUserDetails();

		// Assert
		assertEquals(expectedUserDetailsDto, result); // 메소드의 반환값이 예상한 UserDetailsDto와 일치하는지 검증합
		verify(apiUserResolver).getCurrentUserDto(); // apiUserResolver.getCurrentUserDto() 메소드가 정확히 한 번 호출되었는지 검증
		verify(univFinder, never()).findUniversity(anyLong()); // univFinder는 호출되지 않아야 함


	}
	@Test
	public void UserService_GetUserDetails_ReturnUserDetailsDto_Verified() {
		// 대학교 ID를 가지고 있는 경우의 UserDto 생성
		User user = User.builder()
			.email("e1")
			.uniqueId("u1")
			.providerId("p1")
			.username("u1")
			.nickname("n1")
			.nationality(Nationality.KOREA)
			.preferredLanguage(PreferredLanguage.KOREAN)
			.userRole(UserRole.VERIFIED)
			.userStatus(UserStatus.ACTIVE)
			.build();
		user.setUniversityId(1L);
		UserDto userDto = UserDto.from(user);

		// University 객체 생성
		University university = University.builder()
			.universityName("TestUniversity")
			.universityCode("TestUniversity")
			.build();

		// UserDetailsDto 생성 (예상 결과)
		UserDetailsDto expectedUserDetailsDto = UserDetailsDto.of(userDto, 1L, "TestUniversity");

		// Mock 설정
		when(apiUserResolver.getCurrentUserDto()).thenReturn(userDto);
		when(univFinder.findUniversity(anyLong())).thenReturn(university);

		// Act
		UserDetailsDto result = userService.getUserDetails();

		// Assert
		assertEquals(expectedUserDetailsDto, result); // 메소드의 반환값이 예상한 UserDetailsDto와 일치하는지 검증합
		verify(apiUserResolver).getCurrentUserDto(); // apiUserResolver.getCurrentUserDto() 메소드가 정확히 한 번 호출되었는지 검증
		verify(univFinder).findUniversity(1L);
	}

	@Test
	void updateUserDetails() {
	}

	@Test
	void sendMail() {
	}

	@Test
	void verifyEmailCode() {
	}

	@Test
	void uploadCert() {
	}

	@Test
	void checkNicknameAvailability() {
	}

	@Test
	void updateUserInfo() {
	}

	@Test
	void findVerifyStatus() {
	}
}