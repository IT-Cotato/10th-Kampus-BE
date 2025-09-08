package com.cotato.kampus.domain.product.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.post.api.response.SliceResponse;
import com.cotato.kampus.domain.product.api.request.CreateProductRequest;
import com.cotato.kampus.domain.product.api.request.ProductStatusUpdatable;
import com.cotato.kampus.domain.product.api.request.UpdateProductRequest;
import com.cotato.kampus.domain.product.api.response.ProductCreateResponse;
import com.cotato.kampus.domain.product.api.response.ProductDetailResponse;
import com.cotato.kampus.domain.product.application.ProductService;
import com.cotato.kampus.domain.product.domain.ProductThumbnail;
import com.cotato.kampus.domain.product.enums.ProductSortType;
import com.cotato.kampus.domain.product.enums.ProductStatus;
import com.cotato.kampus.global.common.dto.DataResponse;
import com.cotato.kampus.global.error.exception.ImageException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "중고거래 상품 관리 API", description = "상품 등록, 조회, 수정, 삭제 관리 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/products")
public class ProductController {

	private final ProductService productService;

	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "상품 등록")
	public ResponseEntity<DataResponse<ProductCreateResponse>> createProduct(
		@Valid @ModelAttribute CreateProductRequest request
	) throws ImageException {
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

	@PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "상품 수정")
	public ResponseEntity<DataResponse<Void>> updateProduct(
		@PathVariable Long productId,
		@Valid @ModelAttribute UpdateProductRequest request
	) throws ImageException {
		productService.updateProduct(
			productId,
			request.title(),
			request.price(),
			request.description(),
			request.categoryNames(),
			request.images()
		);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/{productId}")
	@Operation(summary = "상품 상세 조회", description = "중고거래 상품 상세 정보와 유저의 스크랩 여부를 반환")
	public ResponseEntity<DataResponse<ProductDetailResponse>> findProductDetails(
		@PathVariable Long productId
	) {
		return ResponseEntity.ok(DataResponse.from(
				ProductDetailResponse.from(
					productService.findProductDetails(productId)
				)
			)
		);
	}

	@GetMapping("")
	@Operation(summary = "상품 목록 조회", description = "카테고리로 필터링된 목록 반환")
	public ResponseEntity<DataResponse<SliceResponse<ProductThumbnail>>> findProducts(
		@RequestParam(required = false, defaultValue = "1") int page,
		@Parameter(
			name = "size",
			description = "한 페이지 표시할 상품 개수"
		)
		@RequestParam(required = false, defaultValue = "10") int size,
		@Parameter(
			name = "sort",
			description = "정렬 기준 (recent: 최신순, old: 오래된순, scrapCount: 스크랩순)"
		)
		@RequestParam(required = false, defaultValue = "recent") ProductSortType sort,
		@Parameter(
			name = "categoryName",
			description = "카테고리 필터링(전체 조회: 파라미터 미입력, 특정 카테고리 조회: 해당 카테고리명)"
		)
		@RequestParam(required = false) String categoryName
	) {
		return ResponseEntity.ok(DataResponse.from(
				SliceResponse.from(
					productService.findProducts(page, size, sort, categoryName)
				)
			)
		);
	}

	@PatchMapping("/{productId}/status")
	@Operation(summary = "상품 상태 변경", description = "상품 상태 변경 (ACTIVE, SOLD, RESERVED)")
	public ResponseEntity<DataResponse<Void>> updateProductStatus(
		@PathVariable Long productId,
		@RequestParam("productStatus") ProductStatusUpdatable productStatus
	) {
		productService.updateStatus(
			productId,
			ProductStatus.valueOf(productStatus.name())
		);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/my")
	@Operation(summary = "사용자가 등록한 중고거래 상품 목록 조회")
	public ResponseEntity<DataResponse<SliceResponse<ProductThumbnail>>> findMyProducts(
		@RequestParam(required = false, defaultValue = "1") int page,
		@Parameter(name = "size", description = "한 페이지 표시할 상품 개수")
		@RequestParam(required = false, defaultValue = "10") int size
	) {
		return ResponseEntity.ok(DataResponse.from(
				SliceResponse.from(productService.findMyProducts(page, size))
			)
		);
	}
}
