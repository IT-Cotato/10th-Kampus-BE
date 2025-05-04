package com.cotato.kampus.domain.product.api;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.product.application.ProductService;
import com.cotato.kampus.domain.product.api.request.ProductCreateRequest;
import com.cotato.kampus.domain.product.api.response.ProductCreateResponse;
import com.cotato.kampus.global.common.dto.DataResponse;
import com.cotato.kampus.global.error.exception.ImageException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "중고거래(Product) API", description = "중고거래 관련 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/products")
public class ProductController {

	private final ProductService productService;

	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "상품 등록")
	public ResponseEntity<DataResponse<ProductCreateResponse>> createProduct(
		@Valid @ModelAttribute ProductCreateRequest request) throws ImageException {

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
}
