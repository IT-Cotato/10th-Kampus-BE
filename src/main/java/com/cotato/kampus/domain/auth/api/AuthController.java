package com.cotato.kampus.domain.auth.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.auth.application.AuthService;
import com.cotato.kampus.domain.auth.application.RefreshService;
import com.cotato.kampus.domain.auth.domain.ReissuedToken;
import com.cotato.kampus.domain.auth.dto.request.SignupRequest;
import com.cotato.kampus.domain.auth.dto.response.SignupResponse;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "인증/인가", description = "인증/인가 관련 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/auth")
public class AuthController {

	private final AuthService authService;
	private final RefreshService refreshService;

	private static final String ACCESS_TOKEN_HEADER = HttpHeaders.AUTHORIZATION;
	private static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

	@PostMapping("/signup")
	@Operation(summary = "일반 로그인", description = "Client 단에서 소셜 로그인 후 회원가입 요청하는 api")
	public ResponseEntity<DataResponse<SignupResponse>> signup(
		@Parameter(description = "회원가입 요청 정보", schema = @Schema(implementation = SignupRequest.class))
		@RequestBody SignupRequest request) {
		return ResponseEntity.ok(DataResponse.from(SignupResponse.of(
			authService.signup(request.email(), request.uniqueId(), request.providerId(), request.username(),
				request.nickname(), request.nationality(), request.languageCode()))));
	}

	@PostMapping("/reissue")
	@Operation(summary = "토큰 재발급", description = "리프레시 토큰을 통해 토큰 재발급하는 API")
	public ResponseEntity<DataResponse<Void>> reissueAccessToken(final HttpServletRequest request,
		final HttpServletResponse response) {
		ReissuedToken reissuedToken = refreshService.reissueRefreshToken(
			request.getHeader(REFRESH_TOKEN_HEADER));
		response.addHeader(ACCESS_TOKEN_HEADER, reissuedToken.accessToken());
		response.addHeader(REFRESH_TOKEN_HEADER, reissuedToken.refreshToken());
		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	@Operation(summary = "서버 헬스 체크", description = "서버 헬스 체크")
	@GetMapping("/health")
	public ResponseEntity<DataResponse<Void>> health() {
		return ResponseEntity.ok(DataResponse.ok());
	}
}