package com.cotato.kampus.domain.product.dao.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

import com.cotato.kampus.domain.product.dao.entity.ProductEntity;
import com.cotato.kampus.domain.product.enums.ProductStatus;
import com.cotato.kampus.domain.product.factory.ProductEntityFactory;

@DataJpaTest
@ActiveProfiles("test")
class ProductJpaRepositoryTest {

	@Autowired
	private ProductJpaRepository productJpaRepository;

	@AfterEach
	void setUp() {
		productJpaRepository.deleteAll();
	}

	@Test
	void findByKeywordWithStatusPriority() {
		// Given
		String keyword = "테스트";
		LocalDateTime baseTime = LocalDateTime.now();

		List<ProductEntity> products = List.of(
			ProductEntityFactory.create("테스트 상품1", ProductStatus.ACTIVE, baseTime.minusHours(1)),
			ProductEntityFactory.create("테스트 상품2", ProductStatus.ACTIVE, baseTime.minusHours(3)),
			ProductEntityFactory.create("테스트 상품3", ProductStatus.RESERVED, baseTime.minusMinutes(30)),
			ProductEntityFactory.create("테스트 상품4", ProductStatus.SOLD, baseTime.minusMinutes(10)),
			ProductEntityFactory.create("관련없는 상품", ProductStatus.ACTIVE, baseTime),
			ProductEntityFactory.create("테스트 상품5", ProductStatus.DELETED, baseTime.plusMinutes(10))
		);

		productJpaRepository.saveAll(products);

		// When
		Slice<ProductEntity> result = productJpaRepository.findByKeywordWithStatusPriority(
			keyword, Pageable.unpaged());

		// Then
		List<ProductEntity> resultList = result.getContent();

		assertThat(resultList).hasSize(4);

		// 상태 우선순위 확인: ACTIVE > RESERVED > SOLD
		assertThat(resultList.get(0).getProductStatus()).isEqualTo(ProductStatus.ACTIVE);
		assertThat(resultList.get(1).getProductStatus()).isEqualTo(ProductStatus.ACTIVE);
		assertThat(resultList.get(2).getProductStatus()).isEqualTo(ProductStatus.RESERVED);
		assertThat(resultList.get(3).getProductStatus()).isEqualTo(ProductStatus.SOLD);

		// 같은 상태 내에서 최신순 확인
		assertThat(resultList.get(0).getCreatedTime()).isAfter(resultList.get(1).getCreatedTime());
	}
}