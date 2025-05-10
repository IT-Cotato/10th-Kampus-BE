package com.cotato.kampus.domain.product.implement.productCategory;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.product.domain.ProductCategoryMapping;
import com.cotato.kampus.domain.product.implement.port.ProductCategoryMappingRepository;

@ExtendWith(MockitoExtension.class)
class ProductCategoryMappingManagerTest {

	@InjectMocks
	private ProductCategoryMappingManager productCategoryMappingManager;

	@Mock
	private ProductCategoryMappingRepository productCategoryMappingRepository;

	@Test
	@DisplayName("상품에 여러 카테고리를 매핑")
	void saveAll() {
		// Given: 상품 ID와 여러 카테고리 ID가 주어졌을 때
		Long productId = 1L;
		List<Long> categoryIds = Arrays.asList(1L, 2L, 3L);

		// When: 카테고리 매핑을 저장하면
		productCategoryMappingManager.saveAll(productId, categoryIds);

		// Then: 각 카테고리별로 매핑이 저장됨
		ArgumentCaptor<ProductCategoryMapping> productCategoryMappingCaptor = ArgumentCaptor.captor();
		then(productCategoryMappingRepository).should(times(3)).save(productCategoryMappingCaptor.capture());

		List<ProductCategoryMapping> allMappings = productCategoryMappingCaptor.getAllValues();
		assertThat(allMappings).hasSize(3);

		assertThat(allMappings.get(0).getProductId()).isEqualTo(productId);
		assertThat(allMappings.get(0).getCategoryId()).isEqualTo(1L);

		assertThat(allMappings.get(1).getProductId()).isEqualTo(productId);
		assertThat(allMappings.get(1).getCategoryId()).isEqualTo(2L);

		assertThat(allMappings.get(2).getProductId()).isEqualTo(productId);
		assertThat(allMappings.get(2).getCategoryId()).isEqualTo(3L);

	}
}