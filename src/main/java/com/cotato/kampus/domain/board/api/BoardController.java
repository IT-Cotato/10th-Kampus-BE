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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시판(Board) API", description = "게시판 관련 API(게시글 API는 Post)")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/boards")
public class BoardController {

	private final BoardService boardService;

	@GetMapping("/public")
	@Operation(summary = "공용 게시판 목록 조회", description = "공용 게시판 목록을 조회합니다. (학교 게시판 제외)")
	public ResponseEntity<DataResponse<BoardListResponse>> getBoardList() {
		return ResponseEntity.ok(DataResponse.from(
			BoardListResponse.from(
				boardService.getBoardList()))
		);
	}

	@GetMapping("/university")
	@Operation(summary = "대학교 게시판 조회", description = "(재학생 인증된) 유저의 대학교 게시판을 조회합니다.")
	public ResponseEntity<DataResponse<BoardResponse>> getUniversityBoard() {
		return ResponseEntity.ok(DataResponse.from(
			BoardResponse.from(
				boardService.getUniversityBoard()
			)
		));
	}

	@GetMapping("/favorite")
	@Operation(summary = "즐겨찾기 게시판 목록 조회", description = "즐겨찾기에 등록된 게시판 목록을 조회합니다.")
	public ResponseEntity<DataResponse<BoardListResponse>> getFavoriteBoardList() {
		return ResponseEntity.ok(DataResponse.from(
			BoardListResponse.from(
				boardService.getFavoriteBoardList()
			)
		));
	}

	@PostMapping("/favorite/{boardId}")
	@Operation(summary = "게시판 즐겨찾기 추가", description = "특정 게시판을 즐겨찾기에 추가합니다.")
	public ResponseEntity<DataResponse<FavoriteBoardResponse>> addFavoriteBoard(
		@PathVariable Long boardId
	) {
		return ResponseEntity.ok(DataResponse.from(
			FavoriteBoardResponse.of(
				boardService.addFavoriteBoard(boardId)
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
				boardService.removeFavoriteBoard(boardId)
			)
		));
	}
}