package com.cotato.kampus.domain.board.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.board.api.response.FavoriteBoardResponse;
import com.cotato.kampus.domain.board.application.BoardFavoriteService;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시판(Board) API", description = "게시판 관련 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/boards")
public class BoardFavoriteController {

	private final BoardFavoriteService boardFavoriteService;

	@PostMapping("/favorite/{boardId}")
	@Operation(summary = "게시판 즐겨찾기 추가", description = "특정 게시판을 즐겨찾기에 추가합니다.")
	public ResponseEntity<DataResponse<FavoriteBoardResponse>> addFavoriteBoard(
		@PathVariable Long boardId
	) {
		return ResponseEntity.ok(DataResponse.from(
			FavoriteBoardResponse.of(
				boardFavoriteService.addFavoriteBoard(boardId)
			)
		));
	}

	@DeleteMapping("/favorite/{boardId}")
	@Operation(summary = "게시판 즐겨찾기 삭제", description = "특정 게시판을 즐겨찾기에서 삭제합니다.")
	public ResponseEntity<DataResponse<FavoriteBoardResponse>> removeFavoriteBoard(
		@PathVariable Long boardId
	) {
		return ResponseEntity.ok(DataResponse.from(
			FavoriteBoardResponse.of(
				boardFavoriteService.removeFavoriteBoard(boardId)
			)
		));
	}
}
