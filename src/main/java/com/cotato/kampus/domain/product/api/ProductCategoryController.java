package com.cotato.kampus.domain.product.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.product.api.request.CreateProductCategoryRequest;
import com.cotato.kampus.domain.product.api.response.ProductCategoryFindResponse;
import com.cotato.kampus.domain.product.application.ProductCategoryService;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "중고거래 카테고리(ProductCategory) API", description = "중고거래 카테고리 관련 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/products/categories")
public class ProductCategoryController {

	private final ProductCategoryService productCategoryService;

	@PostMapping
	@Operation(summary = "중고거래 카테고리 생성", description = "중고거래에서 사용할 카테고리를 생성합니다. (중복 허용 X)")
	public ResponseEntity<DataResponse<Long>> createCategory(
		@RequestBody CreateProductCategoryRequest request
	) {
			return ResponseEntity.ok(DataResponse.from(
				productCategoryService.createCategory(
					request.categoryName()
				)
			)
		);
	}

	@GetMapping
	@Operation(summary = "중고거래 카테고리 조회", description = "중고거래 상품에 적용 가능한 카테고리 전체 조회")
	public ResponseEntity<DataResponse<ProductCategoryFindResponse>> findAllCategories() {
			return ResponseEntity.ok(DataResponse.from(
				ProductCategoryFindResponse.from(
					productCategoryService.findAllCategories()
				)
			)
		);
	}
}
