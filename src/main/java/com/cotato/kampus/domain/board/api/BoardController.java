package com.cotato.kampus.domain.board.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.board.application.BoardService;
import com.cotato.kampus.domain.board.dto.response.BoardResponse;
import com.cotato.kampus.global.common.dto.DataResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/boards")
public class BoardController {

	private final BoardService boardService;

	@GetMapping("")
	public ResponseEntity<DataResponse<BoardResponse>> getBoardList(){
		return ResponseEntity.ok(DataResponse.from(
				BoardResponse.of(
				boardService.getBoardList()))
			);
	}

	@GetMapping("/favorite")
	public ResponseEntity<DataResponse<BoardResponse>> getFavoriteBoardList(){
		return ResponseEntity.ok(DataResponse.from(
			BoardResponse.of(
				boardService.getFavoriteBoardList()
			)
		));
	}
}
