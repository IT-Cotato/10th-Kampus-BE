package com.cotato.kampus.domain.admin.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.admin.application.AdminService;
import com.cotato.kampus.domain.admin.dto.request.BoardCreateRequest;
import com.cotato.kampus.domain.admin.dto.response.BoardCreateResponse;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "관리자", description = "관리자 페이지 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/admin")
public class AdminController {

	private final AdminService adminService;

	@PostMapping("/boards")
	@Operation(summary = "게시판 생성", description = "게시판을 생성합니다. 학교 게시판이 아닌 경우 universityId를 null로 주세요")
	public ResponseEntity<DataResponse<BoardCreateResponse>> createBoard(
		@Valid @RequestBody BoardCreateRequest request
	) {
			return ResponseEntity.ok(DataResponse.from(
				BoardCreateResponse.from(
					adminService.createBoard(
						request.boardName(),
						request.universityId(),
						request.isCategoryRequired()
					)
				)
			)
		);
	}
}
