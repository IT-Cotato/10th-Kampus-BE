package com.cotato.kampus.domain.product.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.post.api.response.SliceResponse;
import com.cotato.kampus.domain.product.application.ProductScrapService;
import com.cotato.kampus.domain.product.domain.ProductThumbnail;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

	@GetMapping("/scraps")
	@Operation(summary = "스크랩 상품 목록 조회", description = "사용자의 최신 스크랩순으로 조회합니다.")
	public ResponseEntity<DataResponse<SliceResponse<ProductThumbnail>>> findScrapProducts(
		@RequestParam(required = false, defaultValue = "1") int page,
		@Parameter(name = "size", description = "한 페이지 표시할 상품 개수")
		@RequestParam(required = false, defaultValue = "10") int size
	) {
		return ResponseEntity.ok(DataResponse.from(
				SliceResponse.from(
					productScrapService.findScrapProducts(page, size)
				)
			)
		);
	}
}
