package com.cotato.kampus.domain.product.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.product.application.ProductScrapService;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "중고거래 상품 스크랩 API", description = "상품 스크랩 추가, 삭제 및 조회 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/products")
public class ProductScrapController {

	private final ProductScrapService productScrapService;

	@PostMapping("/{productId}/scraps")
	@Operation(summary = "상품 스크랩 추가")
	public ResponseEntity<DataResponse<Void>> addScrap(
		@PathVariable Long productId
	) {
		productScrapService.addScrap(productId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping("/{productId}/scraps")
	@Operation(summary = "상품 스크랩 삭제")
	public ResponseEntity<DataResponse<Void>> removeScrap(
		@PathVariable Long productId
	) {
		productScrapService.removeScrap(productId);
		return ResponseEntity.ok(DataResponse.ok());
	}
}
