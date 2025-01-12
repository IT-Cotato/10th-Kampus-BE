package com.cotato.kampus.domain.auth.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.auth.applicaon.AuthService;
import com.cotato.kampus.domain.auth.dto.request.SignupRequest;
import com.cotato.kampus.domain.auth.dto.response.SignupResponse;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "인증/인가", description = "인증/인가 관련 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	@Operation(summary = "일반 로그인", description = "Client 단에서 소셜 로그인 후 회원가입 요청하는 api")
	public ResponseEntity<DataResponse<SignupResponse>> signup(
		@Parameter(description = "회원가입 요청 정보", schema = @Schema(implementation = SignupRequest.class))
		@RequestBody SignupRequest request) {
		return ResponseEntity.ok(DataResponse.from(SignupResponse.of(
			authService.signup(request.email(), request.uniqueId(), request.providerId(), request.username(),
				request.nickname(), request.nationality()))));
	}

	@Operation(summary = "서버 헬스 체크", description = "서버 헬스 체크")
	@GetMapping("/health")
	public ResponseEntity<DataResponse<Void>> health() {
		return ResponseEntity.ok(DataResponse.ok());
	}
}