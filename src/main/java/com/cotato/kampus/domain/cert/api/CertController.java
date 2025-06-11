package com.cotato.kampus.domain.cert.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
