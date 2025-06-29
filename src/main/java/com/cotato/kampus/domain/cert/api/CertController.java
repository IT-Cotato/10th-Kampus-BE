package com.cotato.kampus.domain.cert.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.cert.api.request.EmailSendRequest;
import com.cotato.kampus.domain.cert.api.request.EmailVerifyRequest;
import com.cotato.kampus.domain.cert.api.response.CertStatusResponse;
import com.cotato.kampus.domain.cert.api.response.RejectReasonResponse;
import com.cotato.kampus.domain.cert.application.CertService;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "대학 인증 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/cert")
public class CertController {

	private final CertService certService;

	@GetMapping("/universites/check")
	@Operation(summary = "대학명이 서버에 존재하는 학교인지 체크", description = "영문 대학명을 입력해주세요")
	public ResponseEntity<DataResponse<Boolean>> checkUnivCode(
		@RequestParam String universityCode
	) {
			return ResponseEntity.ok(DataResponse.from(
				certService.checkUnivCode(universityCode)
			)
		);
	}

	@PostMapping("/email/send")
	@Operation(summary = "대학 이메일 인증 메일 발송")
	public ResponseEntity<DataResponse<Void>> sendEmail(
		@RequestBody EmailSendRequest request
	) {
		certService.sendMail(request.univCode(), request.email());
		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/email/verify")
	@Operation(summary = "이메일 인증 코드 확인", description = "4자리 코드를 입력해주세요")
	public ResponseEntity<DataResponse<Void>> verifyEmailCode(
		@RequestBody EmailVerifyRequest request
	) {
		certService.verifyEmailCode(request.email(), request.code());
		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/clear")
	@Operation(summary = "[테스트용] 본인 인증 초기화", description = "[테스트용] 인증 기록을 삭제하고 UserRole.UNVERIFIED 적용")
	public ResponseEntity<DataResponse<Void>> clear() {
		certService.clear();
		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/status")
	@Operation(summary = "인증 상태 조회", description = "인증 상태와 (반려일 경우) 반려 사유 조회")
	public ResponseEntity<DataResponse<CertStatusResponse>> getCertStatus() {
			return ResponseEntity.ok(DataResponse.from(
				CertStatusResponse.from(
					certService.getCertStatus()
				)
			)
		);
	}

	@GetMapping("/reject-reason")
	@Operation(summary = "서류 반려 사유 조회", description = "서류가 반려된 경우 제출했던 서류 이미지와 사유 조회")
	public ResponseEntity<DataResponse<RejectReasonResponse>> getRejectReason() {
			return ResponseEntity.ok(DataResponse.from(
				RejectReasonResponse.of(
					certService.getRejectReason()
				)
			)
		);
	}
}
