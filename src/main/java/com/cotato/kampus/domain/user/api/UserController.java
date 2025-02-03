package com.cotato.kampus.domain.user.api;

import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.user.application.UserService;
import com.cotato.kampus.domain.user.dto.request.ConfirmMailRequest;
import com.cotato.kampus.domain.user.dto.request.SendMailRequest;
import com.cotato.kampus.domain.user.dto.request.UserDetailsUpdateRequest;
import com.cotato.kampus.domain.user.dto.response.UserDetailsResponse;
import com.cotato.kampus.domain.user.dto.response.ConfirmMailResponse;
import com.cotato.kampus.domain.user.dto.response.SendMailResponse;
import com.cotato.kampus.domain.user.dto.response.UserDetailsUpdateResponse;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "유저 API", description = "User 관련 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/users")
public class UserController {

	private final UserService userService;

	@GetMapping("/details")
	@Operation(summary = "유저 정보 조회 api", description = "닉네임, 국적, 선호 언어 조회")
	public ResponseEntity<DataResponse<UserDetailsResponse>> getUserDetails() {
		return ResponseEntity.ok(
			DataResponse.from(
				UserDetailsResponse.from(
					userService.getUserDetails()
				)
			)
		);
	}

	@PatchMapping("/details")
	@Operation(summary = "소셜 로그인(카카오) 이후 유저 세부정보 저장(변경) api", description = "닉네임, 국적, 선호 언어, 약관 동의 내역 저장")
	public ResponseEntity<DataResponse<UserDetailsUpdateResponse>> updateUserDetails(
		@RequestBody @Valid UserDetailsUpdateRequest request) {
		return ResponseEntity.ok(
			DataResponse.from(
				UserDetailsUpdateResponse.from(
					userService.updateUserDetails(
						request.nickname(),
						request.nationality(),
						request.preferredLanguage(),
						request.personalInfoAgreement(),
						request.privacyPolicyAgreement(),
						request.termsOfServiceAgreement(),
						request.marketingAgreement()
					)
				)
			)
		);
	}

	@PostMapping("/verify/email/send")
	public ResponseEntity<DataResponse<SendMailResponse>> sendVerificationCode(
		@RequestBody SendMailRequest request
	) throws IOException {
			return ResponseEntity.ok(DataResponse.from(
				SendMailResponse.from(
					userService.sendMail(
						request.email(), request.univName()
					)
				)
			)
		);
	}

	@PostMapping("/verify/mail/confirm")
	public ResponseEntity<DataResponse<ConfirmMailResponse>> verifyEmailCode(
		@RequestBody ConfirmMailRequest request
	) throws IOException {
			return ResponseEntity.ok(DataResponse.from(
				ConfirmMailResponse.from(
					userService.verifyEmailCode(
						request.email(), request.univName(), request.code()
					)
				)
			)
		);
	}
}