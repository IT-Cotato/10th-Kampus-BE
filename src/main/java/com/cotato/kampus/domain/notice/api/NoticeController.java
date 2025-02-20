package com.cotato.kampus.domain.notice.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.notice.application.NoticeService;
import com.cotato.kampus.domain.notice.dto.request.NoticeCreateRequest;
import com.cotato.kampus.domain.notice.dto.response.NoticeCreateResponse;
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
}