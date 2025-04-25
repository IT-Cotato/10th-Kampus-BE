package com.cotato.kampus.domain.board.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.board.application.BoardCategoryService;
import com.cotato.kampus.domain.board.api.response.BoardCategoryFindResponse;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시판(Board) API", description = "게시판 관련 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/boards")
public class BoardCategoryController {

	private final BoardCategoryService boardCategoryService;

	@GetMapping("/boards/{boardId}/categories")
	@Operation(summary = "게시판에 적용되는 카테고리 조회",
		description = "boardId에 해당하는 게시판에 적용되는 카테고리를 조회합니다.")
	public ResponseEntity<DataResponse<BoardCategoryFindResponse>> findCategories(
		@PathVariable Long boardId
	) {
		return ResponseEntity.ok(
			DataResponse.from(
				BoardCategoryFindResponse.from(
					boardCategoryService.findCategories(boardId)
				)
			)
		);
	}
}
