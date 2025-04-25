package com.cotato.kampus.domain.category.implement.port;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.cotato.kampus.domain.category.dao.repository.CategoryRepositoryImpl;
import com.cotato.kampus.domain.category.domain.Category;

@ActiveProfiles("test")
@DataJpaTest
@Import(CategoryRepositoryImpl.class)
class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	@DisplayName("카테고리 저장 및 ID로 조회 테스트")
	void saveAndFindById() {
		// given
		String categoryName = "테스트 카테고리";
		Category category = Category.builder()
			.categoryName(categoryName)
			.build();

		// when
		Category savedCategory = categoryRepository.save(category);
		Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

		// then
		assertThat(foundCategory).isPresent();
		assertThat(foundCategory.get().getId()).isEqualTo(savedCategory.getId());
		assertThat(foundCategory.get().getCategoryName()).isEqualTo(categoryName);
	}

	@Test
	@DisplayName("카테고리명으로 조회 테스트")
	void findCategoryName() {
		// given
		String categoryName = "조회용 카테고리";
		Category category = Category.builder()
			.categoryName(categoryName)
			.build();

		categoryRepository.save(category);

		// when
		Optional<Category> foundCategory = categoryRepository.findByCategoryName(categoryName);

		// then
		assertThat(foundCategory).isPresent();
		assertThat(foundCategory.get().getCategoryName()).isEqualTo(categoryName);
	}

	@Test
	@DisplayName("존재하지 않는 카테고리명으로 조회 실패 테스트")
	void findByCategoryName_NotFound() {
		// given
		String nonExistentCategoryName = "존재하지 않는 카테고리";

		// when
		Optional<Category> foundCategory = categoryRepository.findByCategoryName(nonExistentCategoryName);

		// then
		assertThat(foundCategory).isEmpty();
	}

	@Test
	@DisplayName("전체 카테고리 조회 테스트")
	void findAll() {
		// given
		int initialSize = categoryRepository.findAll().size();
		List<String> categoryNames = List.of("카테고리1", "카테고리2", "카테고리3");
		for(String name : categoryNames) {
			Category category = Category.builder()
				.categoryName(name)
				.build();
			categoryRepository.save(category);
		}

		// when
		List<Category> categories = categoryRepository.findAll();

		// then
		assertThat(categories.size()).isEqualTo(initialSize + categoryNames.size());

		List<String> foundCategoryNames = categories.stream()
			.map(Category::getCategoryName)
			.toList();

		for(String name : categoryNames) {
			assertThat(foundCategoryNames).contains(name);
		}
	}
}