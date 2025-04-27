package com.cotato.kampus.domain.category.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.category.domain.Category;
import com.cotato.kampus.domain.category.implement.CategoryAppender;
import com.cotato.kampus.domain.category.implement.CategoryFinder;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.domain.user.enums.UserStatus;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

	@Mock
	private ApiUserResolver apiUserResolver;

	@Mock
	private CategoryAppender categoryAppender;

	@Mock
	private UserValidator userValidator;

	@Mock
	private CategoryFinder categoryFinder;

	@InjectMocks
	private CategoryService categoryService;

	// 테스트에서 사용할 사용자 객체들을 멤버 변수로 선언
	private UserDto adminUser;
	private UserDto normalUser;

	@BeforeEach
	void setUp() {
		// 각 테스트 실행 전에 사용자 객체 초기화
		adminUser = new UserDto(
			1L,                     // id
			"admin@example.com",    // email
			"unique123",            // uniqueId
			"provider123",          // providerId
			"Admin User",           // username
			"admin",                // nickname
			1L,                     // universityId
			"profile.jpg",          // profileImage
			Nationality.KOREA,     // nationality
			PreferredLanguage.KOREAN, // preferredLanguage
			"device123",            // deviceToken
			UserRole.ADMIN,         // userRole
			UserStatus.ACTIVE       // userStatus
		);

		normalUser = new UserDto(
			2L,                     // id
			"user@example.com",     // email
			"unique456",            // uniqueId
			"provider456",          // providerId
			"Normal User",          // username
			"user",                 // nickname
			2L,                     // universityId
			"profile.jpg",          // profileImage
			Nationality.KOREA,     // nationality
			PreferredLanguage.KOREAN, // preferredLanguage
			"device456",            // deviceToken
			UserRole.VERIFIED,      // userRole - 일반 사용자
			UserStatus.ACTIVE       // userStatus
		);
	}

	@Test
	@DisplayName("카테고리 생성 성공 테스트 - 관리자 권한")
	void createCategory_Success() {
		// given
		String categoryName = "새 카테고리";
		Category newCategory = Category.builder()
			.id(1L)
			.categoryName(categoryName)
			.build();

		when(apiUserResolver.getCurrentUserDto()).thenReturn(adminUser);
		doNothing().when(userValidator).validateAdminAccess(adminUser);
		when(categoryAppender.append(categoryName)).thenReturn(newCategory);

		// when
		Long categoryId = categoryService.createCategory(categoryName);

		// then
		assertThat(categoryId).isEqualTo(1L);
		verify(apiUserResolver).getCurrentUserDto();
		verify(userValidator).validateAdminAccess(adminUser);
		verify(categoryAppender).append(categoryName);
	}

	@Test
	@DisplayName("카테고리 생성 실패 테스트 - 관리자 권한 없음")
	void createCategory_Failure_NotAdmin() {
		// given
		String categoryName = "새 카테고리";

		when(apiUserResolver.getCurrentUserDto()).thenReturn(normalUser);
		doThrow(new AppException(ErrorCode.USER_NOT_ADMIN))
			.when(userValidator).validateAdminAccess(normalUser);

		// when & then
		assertThatThrownBy(() -> categoryService.createCategory(categoryName))
			.isInstanceOf(AppException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_ADMIN);

		verify(apiUserResolver).getCurrentUserDto();
		verify(userValidator).validateAdminAccess(normalUser);
	}

	@Test
	@DisplayName("모든 카테고리 조회 테스트")
	void findAllCategory_Success() {
		// given
		List<Category> expectedCategories = Arrays.asList(
			Category.builder().id(1L).categoryName("카테고리1").build(),
			Category.builder().id(2L).categoryName("카테고리2").build(),
			Category.builder().id(3L).categoryName("카테고리3").build()
		);

		when(categoryFinder.findAll()).thenReturn(expectedCategories);

		// when
		List<Category> results = categoryService.findAllCategory();

		// then
		assertThat(results).isNotNull();
		assertThat(results).hasSize(3);
		assertThat(results).isEqualTo(expectedCategories);
		verify(categoryFinder).findAll();
	}
}