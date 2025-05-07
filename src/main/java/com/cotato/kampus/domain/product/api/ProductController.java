package com.cotato.kampus.domain.product.api;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.product.application.ProductService;
import com.cotato.kampus.domain.product.api.request.CreateProductRequest;
import com.cotato.kampus.domain.product.api.response.ProductCreateResponse;
import com.cotato.kampus.global.common.dto.DataResponse;
import com.cotato.kampus.global.error.exception.ImageException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "중고거래 상품 관리 API", description = "상품 등록, 조회, 수정, 삭제 및 스크랩 관리 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/products")
public class ProductController {

	private final ProductService productService;

	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "상품 등록")
	public ResponseEntity<DataResponse<ProductCreateResponse>> createProduct(
		@Valid @ModelAttribute CreateProductRequest request) throws ImageException {

		return ResponseEntity.ok(DataResponse.from(
				ProductCreateResponse.of(
					productService.createProduct(
						request.title(),
						request.price(),
						request.description(),
						request.categoryNames(),
						request.images()
					)
				)
			)
		);
	}

	@DeleteMapping("/{productId}")
	@Operation(summary = "상품 삭제")
	public ResponseEntity<DataResponse<Void>> deleteProduct(
		@PathVariable Long productId
	) {
		productService.deleteProduct(productId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/{productId}/scraps")
	@Operation(summary = "상품 스크랩 추가")
	public ResponseEntity<DataResponse<Void>> addScrap(
		@PathVariable Long productId
	) {
		productService.addScrap(productId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping("/{productId}/scraps")
	@Operation(summary = "상품 스크랩 삭제")
	public ResponseEntity<DataResponse<Void>> removeScrap(
		@PathVariable Long productId
	) {
		productService.removeScrap(productId);
		return ResponseEntity.ok(DataResponse.ok());
	}
}
