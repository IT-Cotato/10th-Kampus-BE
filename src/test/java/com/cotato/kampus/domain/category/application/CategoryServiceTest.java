package com.cotato.kampus.domain.category.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.category.domain.Category;
import com.cotato.kampus.domain.category.implement.CategoryManager;
import com.cotato.kampus.domain.category.implement.CategoryFinder;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.helper.TestUserHelper;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

	@Mock
	private CategoryManager categoryManager;

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
		adminUser = TestUserHelper.createUserDto(1L,  null, UserRole.ADMIN);
		normalUser = TestUserHelper.createUserDto(2L, 2L, UserRole.VERIFIED);
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

		doNothing().when(userValidator).validateAdminAccess();
		when(categoryFinder.existsByCategoryName(categoryName)).thenReturn(false);
		when(categoryManager.append(categoryName)).thenReturn(newCategory);

		// when
		Long categoryId = categoryService.createCategory(categoryName);

		// then
		assertThat(categoryId).isEqualTo(1L);
		verify(userValidator).validateAdminAccess();
		verify(categoryFinder).existsByCategoryName(categoryName);
		verify(categoryManager).append(categoryName);
	}

	@Test
	@DisplayName("카테고리 생성 실패 테스트 - 관리자 권한 없음")
	void createCategory_Failure_NotAdmin() {
		// given
		String categoryName = "새 카테고리";

		doThrow(new AppException(ErrorCode.USER_NOT_ADMIN))
			.when(userValidator).validateAdminAccess();

		// when & then
		assertThatThrownBy(() -> categoryService.createCategory(categoryName))
			.isInstanceOf(AppException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_ADMIN);

		verify(userValidator).validateAdminAccess();
		verify(categoryFinder, never()).existsByCategoryName(anyString());
		verify(categoryManager, never()).append(anyString());
	}

	@Test
	@DisplayName("카테고리 생성 실패 테스트 - 카테고리 이름 중복")
	void createCategory_Failure_DuplicateName() {
		// given
		String categoryName = "중복 카테고리";

		// 관리자 검증은 통과하지만 카테고리 이름이 중복
		doNothing().when(userValidator).validateAdminAccess();
		when(categoryFinder.existsByCategoryName(categoryName)).thenReturn(true);

		// when & then
		assertThatThrownBy(() -> categoryService.createCategory(categoryName))
			.isInstanceOf(AppException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.CATEGORY_DUPLICATED);

		verify(userValidator).validateAdminAccess();
		verify(categoryFinder).existsByCategoryName(categoryName);
		verify(categoryManager, never()).append(anyString());
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

	@Test
	@DisplayName("카테고리 수정 테스트 - 성공")
	void updateCategory_Success() {
		// given
		Long categoryId = 1L;
		String categoryName = "카테고리";
		String newCategoryName = "변경된 카테고리";

		Category category = Category.builder()
			.id(categoryId)
			.categoryName(categoryName)
			.build();


		doNothing().when(userValidator).validateAdminAccess();
		when(categoryFinder.find(categoryId)).thenReturn(category);

		// when
		categoryService.updateCategory(categoryId, newCategoryName);

		// then
		ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
		verify(categoryManager).update(captor.capture());

		Category updatedCategory = captor.getValue();
		assertEquals(newCategoryName, updatedCategory.getCategoryName());

		verify(userValidator).validateAdminAccess();
		verify(categoryFinder).find(categoryId);
	}

	@Test
	@DisplayName("카테고리 수정 테스트 - 관리자 권한 없음 실패")
	void updateCategory_Failure_NotAdmin() {
		// given
		Long categoryId = 1L;
		String newCategoryName = "변경된 카테고리";

		doThrow(new AppException(ErrorCode.USER_NOT_ADMIN))
			.when(userValidator).validateAdminAccess();

		// when & then
		AppException exception = assertThrows(AppException.class,
			() -> categoryService.updateCategory(categoryId, newCategoryName));

		assertThat(exception)
			.extracting(AppException::getErrorCode)
			.isEqualTo(ErrorCode.USER_NOT_ADMIN);

		verify(userValidator).validateAdminAccess();
		verifyNoInteractions(categoryFinder);
	}

}