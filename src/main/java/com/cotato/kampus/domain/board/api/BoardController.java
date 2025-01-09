package com.cotato.kampus.domain.board.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.board.application.BoardService;
import com.cotato.kampus.domain.board.dto.response.BoardListResponse;
import com.cotato.kampus.domain.board.dto.response.BoardResponse;
import com.cotato.kampus.domain.board.dto.response.FavoriteBoardResponse;
import com.cotato.kampus.global.common.dto.DataResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/boards")
public class BoardController {

	private final BoardService boardService;

	@GetMapping("")
	public ResponseEntity<DataResponse<BoardListResponse>> getBoardList(){
		return ResponseEntity.ok(DataResponse.from(
				BoardListResponse.of(
				boardService.getBoardList()))
			);
	}

	@GetMapping("/favorite")
	public ResponseEntity<DataResponse<BoardListResponse>> getFavoriteBoardList(){
		return ResponseEntity.ok(DataResponse.from(
			BoardListResponse.of(
				boardService.getFavoriteBoardList()
			)
		));
	}

	@PostMapping("/favorite/{boardId}")
	public ResponseEntity<DataResponse<FavoriteBoardResponse>> addFavoriteBoard(
		@PathVariable Long boardId
	){
		return ResponseEntity.ok(DataResponse.from(
			FavoriteBoardResponse.of(
				boardService.addFavoriteBoard(boardId)
			)
		));
	}

	@DeleteMapping("favorite/{boardId}")
	public ResponseEntity<DataResponse<FavoriteBoardResponse>> removeFavoriteBoard(
		@PathVariable Long boardId
	){
		return ResponseEntity.ok(DataResponse.from(
			FavoriteBoardResponse.of(
				boardService.removeFavoriteBoard(boardId)
			)
		));
	}
}
