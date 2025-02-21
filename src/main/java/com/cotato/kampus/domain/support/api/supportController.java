package com.cotato.kampus.domain.support.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.support.application.SupportService;
import com.cotato.kampus.domain.support.dto.request.CreateInquiryRequest;
import com.cotato.kampus.global.common.dto.DataResponse;
import com.cotato.kampus.global.error.exception.ImageException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "문의 및 신고 API", description = "1:1 문의 및 신고 관련 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/support")
public class supportController {

	private final SupportService supportService;

	@PostMapping(value = "/inquiry", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "1:1 문의 등록", description = "1:1 문의글을 등록합니다.")
	public ResponseEntity<DataResponse<Void>> createInquiry(
		@Valid @ModelAttribute CreateInquiryRequest request
	) throws ImageException {
		supportService.createInquiry(
			request.title(),
			request.content(),
			request.images() == null ? List.of() : request.images()
		);
		return ResponseEntity.ok(DataResponse.ok());
	}
}
