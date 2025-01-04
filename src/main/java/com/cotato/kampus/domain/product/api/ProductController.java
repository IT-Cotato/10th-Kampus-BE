package com.cotato.kampus.domain.product.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.product.application.ProductService;
import com.cotato.kampus.domain.product.dto.request.ProductCreateRequest;
import com.cotato.kampus.domain.product.dto.response.ProductCreateResponse;
import com.cotato.kampus.global.common.dto.DataResponse;
import com.cotato.kampus.global.error.exception.ImageException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/products")
public class ProductController {

	private final ProductService productService;

	@PostMapping("")
	public ResponseEntity<DataResponse<ProductCreateResponse>> createProduct(
		@RequestPart ProductCreateRequest request,
		@RequestPart List<MultipartFile> images) throws ImageException {

		return ResponseEntity.ok(DataResponse.from(
				ProductCreateResponse.of(
					productService.createProduct(
						request.title(),
						request.sellPrice(),
						request.description(),
						request.productCategory(),
						images
					)
				)
			)
		);
	}
}
