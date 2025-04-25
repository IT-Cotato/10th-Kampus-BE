package com.cotato.kampus.domain.category.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.category.api.response.CategoryListResponse;
import com.cotato.kampus.domain.category.application.CategoryService;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "카테고리", description = "카테고리 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/categories")
public class CategoryController {

	private final CategoryService categoryService;

	@PostMapping("")
	@Operation(summary = "카테고리 생성", description = "게시판 생성 시 사용할 카테고리를 생성합니다. (중복 허용 X)")
	public ResponseEntity<DataResponse<Long>> createCategory(
		@RequestParam String categoryName) {
		return ResponseEntity.ok(DataResponse.from(
				categoryService.createCategory(categoryName)
			)
		);
	}

	@GetMapping("")
	@Operation(summary = "카테고리 목록 조회", description = "게시판 생성 시 사용 가능한 카테고리 목록을 조회합니다.")
	public ResponseEntity<DataResponse<CategoryListResponse>> findAllCategory() {
		return ResponseEntity.ok(DataResponse.from(
				CategoryListResponse.from(
					categoryService.findAllCategory()
				)
			)
		);
	}
}
