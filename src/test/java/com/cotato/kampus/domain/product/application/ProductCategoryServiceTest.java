package com.cotato.kampus.domain.product.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.product.domain.ProductCategory;
import com.cotato.kampus.domain.product.domain.ProductCategoryInfo;
import com.cotato.kampus.domain.product.implement.port.ProductCategoryRepository;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ProductCategoryServiceTest {

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Test
	@DisplayName("카테고리 조회 테스트 - 성공")
	void findAllCategories_success() {
		// Given
		ProductCategory category1 = productCategoryRepository.save(ProductCategory.builder().categoryName("카테고리1").build());
		ProductCategory category2 = productCategoryRepository.save(ProductCategory.builder().categoryName("카테고리2").build());

		// When
		List<ProductCategoryInfo> categories = productCategoryService.findAllCategories();

		// Then
		assertThat(categories.size()).isEqualTo(2);
		assertThat(categories.get(0).productCategoryId()).isEqualTo(category1.getId());
	}
}
