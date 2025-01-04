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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<DataResponse<SignupResponse>> signup(@RequestBody SignupRequest request) {
		return ResponseEntity.ok(DataResponse.from(
				SignupResponse.of(
					authService.signup(
						request.email(),
						request.uniqueId(),
						request.providerId(),
						request.password(),
						request.username(),
						request.nickname(),
						request.nationality())
				)
			)
		);
	}

	@GetMapping("/health")
	public ResponseEntity<DataResponse<Void>> health() {
		return ResponseEntity.ok(DataResponse.ok());
	}
}
