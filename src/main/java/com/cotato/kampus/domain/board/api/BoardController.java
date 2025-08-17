package com.cotato.kampus.domain.board.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.board.application.BoardService;
import com.cotato.kampus.domain.board.api.response.BoardListResponse;
import com.cotato.kampus.domain.board.api.response.BoardWithDescriptionResponse;
import com.cotato.kampus.domain.board.api.response.HomePostThumbnailsResponse;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시판(Board) API", description = "게시판 관련 API")
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
	@Operation(summary = "대학교 게시판 최신 글 미리보기 (홈화면)", description = "재학생 인증된 유저의 대학 게시판 최신글 미리보기. 인증 안된 경우 예외처리")
	public ResponseEntity<DataResponse<HomePostThumbnailsResponse>> getUniversityBoard() {
		return ResponseEntity.ok(DataResponse.from(
			HomePostThumbnailsResponse.from(
				boardService.getUniversityBoardPreview()
			)
		));
	}

	@GetMapping("/favorite")
	@Operation(summary = "즐겨찾기 게시판 목록 조회 (홈화면)", description = "즐겨찾기에 등록된 게시판과 각 게시판의 최신 글을 조회합니다.")
	public ResponseEntity<DataResponse<HomePostThumbnailsResponse>> getFavoriteBoardPreview() {
		return ResponseEntity.ok(DataResponse.from(
			HomePostThumbnailsResponse.from(
				boardService.getFavoriteBoardPreview()
			)
		));
	}

	@GetMapping("/trending")
	@Operation(summary = "트렌딩 게시판 미리보기 (홈화면)", description = "트렌딩 게시판의 최근 5개 게시글+게시판을 조회합니다. (타학교 게시판은 제외)")
	public ResponseEntity<DataResponse<HomePostThumbnailsResponse>> getTrendingPreview() {
		return ResponseEntity.ok(
			DataResponse.from(
				HomePostThumbnailsResponse.from(
					boardService.getTrendingPreview()
				)
			)
		);
	}

	@GetMapping("/{boardId}")
	@Operation(summary = "게시판 상세 조회 (유저 버전)", description = "게시판 정보와 즐겨찾기 여부를 조회합니다.")
	public ResponseEntity<DataResponse<BoardWithDescriptionResponse>> getBoardWithDescription(
		@PathVariable Long boardId
	) {
		return ResponseEntity.ok(DataResponse.from(
			BoardWithDescriptionResponse.from(
				boardService.getBoard(boardId)
			)
		));
	}
}