package com.cotato.kampus.domain.notice.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.notice.application.NoticeService;
import com.cotato.kampus.domain.notice.dto.request.NoticeCreateRequest;
import com.cotato.kampus.domain.notice.dto.request.NoticeUpdateRequest;
import com.cotato.kampus.domain.notice.dto.response.NoticeCreateResponse;
import com.cotato.kampus.domain.notice.dto.response.NoticeDetailResponse;
import com.cotato.kampus.domain.notice.dto.response.NoticeListResponse;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "공지사항", description = "공지사항 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/notices")
public class NoticeController {

	private final NoticeService noticeService;

	@PostMapping()
	@Operation(summary = "공지사항 생성", description = "공지사항을 생성합니다.")
	public ResponseEntity<DataResponse<NoticeCreateResponse>> createNotice(
		@Valid @RequestBody NoticeCreateRequest request
	) {
		return ResponseEntity.ok(
			DataResponse.from(
				NoticeCreateResponse.of(
					noticeService.createNotice(request.title(), request.content())
				)
			)
		);
	}

	@GetMapping()
	@Operation(summary = "공지사항 목록 조회", description = "공지사항 목록을 조회합니다.")
	public ResponseEntity<DataResponse<NoticeListResponse>> getNotices(
		@RequestParam(value = "page", defaultValue = "1") int page
	) {
		return ResponseEntity.ok(DataResponse.from(
				NoticeListResponse.from(
					noticeService.getNotices(page)
				)
			)
		);
	}

	@GetMapping("/{noticeId}")
	@Operation(summary = "공지사항 조회", description = "공지사항을 조회합니다.")
	public ResponseEntity<DataResponse<NoticeDetailResponse>> getNotice(
		@PathVariable Long noticeId
	) {
		return ResponseEntity.ok(
			DataResponse.from(
				NoticeDetailResponse.from(
					noticeService.getNotice(noticeId)
				)
			)
		);
	}

	@DeleteMapping("/{noticeId}")
	@Operation(summary = "공지사항 삭제", description = "공지사항을 삭제합니다.")
	public ResponseEntity<DataResponse<Void>> deleteNotice(
		@PathVariable Long noticeId
	) {
		noticeService.deleteNotice(noticeId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@PatchMapping("/{noticeId}")
	@Operation(summary = "공지사항 수정", description = "공지사항을 수정합니다.")
	public ResponseEntity<DataResponse<Void>> updateNotice(
		@PathVariable Long noticeId,
		@Valid @RequestBody NoticeUpdateRequest request
	) {
		noticeService.updateNotice(noticeId, request.title(), request.content());
		return ResponseEntity.ok(DataResponse.ok());
	}
}

