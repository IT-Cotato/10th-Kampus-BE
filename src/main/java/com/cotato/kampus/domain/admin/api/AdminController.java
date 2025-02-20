package com.cotato.kampus.domain.admin.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.admin.application.AdminService;
import com.cotato.kampus.domain.admin.dto.request.BoardCreateRequest;
import com.cotato.kampus.domain.admin.dto.request.BoardUpdateRequest;
import com.cotato.kampus.domain.admin.dto.request.CardNewsCreateRequest;
import com.cotato.kampus.domain.admin.dto.response.AdminBoardListResponse;
import com.cotato.kampus.domain.admin.dto.response.BoardCreateResponse;
import com.cotato.kampus.domain.admin.dto.response.BoardInfoResponse;
import com.cotato.kampus.domain.admin.dto.response.StudentVerificationListResponse;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.post.dto.response.AdminCardNewsListResponse;
import com.cotato.kampus.global.common.dto.DataResponse;
import com.cotato.kampus.global.error.exception.ImageException;

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

	@PostMapping(value = "/boards", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "게시판 생성", description = "게시판을 생성합니다. 학교 게시판이 아닌 경우 universityId를 null로 주세요")
	public ResponseEntity<DataResponse<BoardCreateResponse>> createBoard(
		@Valid @ModelAttribute BoardCreateRequest request
	) {
		return ResponseEntity.ok(DataResponse.from(
				BoardCreateResponse.from(
					adminService.createBoard(
						request.boardName(),
						request.description(),
						request.universityName(),
						request.isCategoryRequired()
					)
				)
			)
		);
	}

	@GetMapping("/boards")
	@Operation(summary = "게시판 목록 조회", description = "전체 게시판 목록을 조회합니다. " +
		"특정 상태를 조회하려면 status 파라미터를 사용하고, 전체 게시판 조회 시에는 status를 비워두시면 됩니다.")
	public ResponseEntity<DataResponse<AdminBoardListResponse>> getBoards(
		@RequestParam(value = "status", required = false) BoardStatus boardStatus
	) {
		return ResponseEntity.ok(DataResponse.from(
			AdminBoardListResponse.from(
				adminService.getBoards(boardStatus)
			)
		));
	}

	@GetMapping("/boards/{boardId}")
	@Operation(summary = "게시판 조회", description = "게시판 세부 정보를 조회합니다.")
	public ResponseEntity<DataResponse<BoardInfoResponse>> getBoards(
		@PathVariable Long boardId) {
		return ResponseEntity.ok(DataResponse.from(
			BoardInfoResponse.from(
				adminService.getBoard(boardId)
			)
		));
	}

	@PutMapping("/boards/{boardId}")
	@Operation(summary = "게시판 수정", description = "게시판을 수정합니다. 학교를 변경하거나 새로 지정할 수 없습니다.")
	public ResponseEntity<DataResponse<Void>> updateBoard(
		@PathVariable Long boardId,
		@RequestBody BoardUpdateRequest request
	) {
		adminService.updateBoard(
			boardId,
			request.boardName(),
			request.description(),
			request.isCategoryRequired()
		);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/boards/{boardId}/inactive")
	@Operation(summary = "게시판 보관(비활성화)", description = "게시판을 비활성화 합니다.")
	public ResponseEntity<DataResponse<Void>> inactiveBoard(
		@PathVariable Long boardId
	) {
		adminService.inactiveBoard(boardId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping("/boards/{boardId}")
	@Operation(summary = "게시판 삭제", description = "게시판을 삭제합니다. 30일간 삭제 대기 상태이며 이후 영구 삭제됩니다.")
	public ResponseEntity<DataResponse<Void>> deleteBoard(
		@PathVariable Long boardId
	) {
		adminService.pendingBoard(boardId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/boards/{boardId}/active")
	@Operation(summary = "게시판 활성화", description = "보관된 게시판을 다시 활성화 합니다.")
	public ResponseEntity<DataResponse<Void>> activeBoard(
		@PathVariable Long boardId
	) {
		adminService.activeBoard(boardId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/student-verifications")
	@Operation(summary = "재학생 인증 목록 조회", description = "재학생 인증 목록을 최신순으로 조회합니다.")
	public ResponseEntity<DataResponse<StudentVerificationListResponse>> getStudentVerifications(
		@RequestParam(required = false, defaultValue = "1") int page) {
		return ResponseEntity.ok(DataResponse.from(
				StudentVerificationListResponse.from(
					adminService.getVerifications(page)
				)
			)
		);
	}

	@PostMapping("/student-verifications/{verificationRecordId}/approve")
	@Operation(summary = "재학생 인증 승인", description = "재학생 인증 요청을 승인합니다.")
	public ResponseEntity<DataResponse<Void>> approveStudentVerification(
		@PathVariable Long verificationRecordId
	) {
		adminService.approveStudentVerification(verificationRecordId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/student-verifications/{verificationRecordId}/reject")
	@Operation(summary = "재학생 인증 반려", description = "재학생 인증 요청을 반려합니다.")
	public ResponseEntity<DataResponse<Void>> rejectStudentVerification(
		@PathVariable Long verificationRecordId
	) {
		adminService.rejectStudentVerification(verificationRecordId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping(value = "/cardNews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "카드뉴스 등록", description = "카드뉴스를 등록합니다.")
	public ResponseEntity<DataResponse<Void>> createCardNews(
		@Valid @ModelAttribute CardNewsCreateRequest request
	) throws ImageException {
		adminService.createCardNews(
			request.title(),
			request.content(),
			request.images()
		);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping(value = "/cardNews")
	@Operation(summary = "카드뉴스 조회", description = "카드뉴스 목록을 조회합니다,")
	public ResponseEntity<DataResponse<AdminCardNewsListResponse>> getAllCardNews(
		@RequestParam(required = false, defaultValue = "1") int page
	) {
		return ResponseEntity.ok(DataResponse.from(
			AdminCardNewsListResponse.from(
				adminService.getAllCardNews(page)
			)
		));
	}
}