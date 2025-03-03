package com.cotato.kampus.domain.user.api;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.user.application.UserService;
import com.cotato.kampus.domain.user.dto.request.ConfirmMailRequest;
import com.cotato.kampus.domain.user.dto.request.NicknameCheckRequest;
import com.cotato.kampus.domain.user.dto.request.SendMailRequest;
import com.cotato.kampus.domain.user.dto.request.UserDetailsUpdateRequest;
import com.cotato.kampus.domain.user.dto.request.UserInfoUpdateRequest;
import com.cotato.kampus.domain.user.dto.response.ConfirmMailResponse;
import com.cotato.kampus.domain.user.dto.response.NicknameCheckResponse;
import com.cotato.kampus.domain.user.dto.response.SendMailResponse;
import com.cotato.kampus.domain.user.dto.response.UserDetailsResponse;
import com.cotato.kampus.domain.user.dto.response.UserDetailsUpdateResponse;
import com.cotato.kampus.domain.user.dto.response.UserInfoUpdateResponse;
import com.cotato.kampus.domain.user.dto.response.VerifyStatusResponse;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;
import com.cotato.kampus.global.common.dto.DataResponse;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.global.error.exception.ImageException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "유저 API", description = "User 관련 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/users")
public class UserController {

	private final UserService userService;
	private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

	@GetMapping("/details")
	@Operation(summary = "유저 정보 조회 api", description = "닉네임, 국적, 선호 언어, 학교(인증한 유저), setup 필요여부 조회")
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
						Nationality.fromName(request.nationality()),
						PreferredLanguage.fromName(request.preferredLanguage()),
						request.personalInfoAgreement(),
						request.privacyPolicyAgreement(),
						request.termsOfServiceAgreement(),
						request.marketingAgreement()
					)
				)
			)
		);
	}

	@PatchMapping("/info")
	@Operation(summary = "[마이페이지] 유저 정보 수정", description = "닉네임, 선호 언어 수정(첫 로그인 이후에는 이거말고 /details 사용하세요!!!!)")
	public ResponseEntity<DataResponse<UserInfoUpdateResponse>> updateUserInfo(
		@RequestBody @Valid UserInfoUpdateRequest request) {
		return ResponseEntity.ok(
			DataResponse.from(
				UserInfoUpdateResponse.from(
					userService.updateUserInfo(
						request.nickname(),
						PreferredLanguage.fromName(request.preferredLanguage())
					)
				)
			)
		);
	}

	@PostMapping("/check-nickname")
	@Operation(summary = "닉네임 중복 체크", description = "입력된 닉네임의 사용 가능 여부를 확인합니다.")
	public ResponseEntity<DataResponse<NicknameCheckResponse>> checkNicknameAvailability(
		@RequestBody @Valid NicknameCheckRequest request) {
		return ResponseEntity.ok(
			DataResponse.from(
				NicknameCheckResponse.from(userService.checkNicknameAvailability(request.nickname())
				)
			)
		);
	}

	@PostMapping("/verify/email/send")
	@Operation(summary = "학교 메일 인증 코드 요청", description = "학교 이메일로 인증 코드를 요청합니다.")
	public ResponseEntity<DataResponse<SendMailResponse>> sendVerificationCode(
		@RequestBody SendMailRequest request
	) throws IOException {
		return ResponseEntity.ok(DataResponse.from(
				SendMailResponse.from(
					userService.sendMail(
						request.email(), request.universityName()
					)
				)
			)
		);
	}

	@PostMapping("/verify/mail/confirm")
	@Operation(summary = "인증 코드 확인", description = "이메일로 수신한 인증 코드(4자리)를 제출합니다. 일치하는 경우 재학생 자격으로 변경됩니다.")
	public ResponseEntity<DataResponse<ConfirmMailResponse>> verifyEmailCode(
		@RequestBody ConfirmMailRequest request
	) throws IOException {
		return ResponseEntity.ok(DataResponse.from(
				ConfirmMailResponse.from(
					userService.verifyEmailCode(
						request.email(), request.universityName(), request.code()
					)
				)
			)
		);
	}

	@PostMapping(value = "/verify/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "재학생 서류 사진 제출", description = "재학생 인증 서류 사진을 제출합니다.")
	public ResponseEntity<DataResponse<Void>> uploadCert(
		@RequestParam("universityName") @NotNull String universityName,
		@RequestPart("certImage") MultipartFile certImage
	) throws ImageException {
		if (certImage.isEmpty()) {
			throw new AppException(ErrorCode.EMPTY_FILE_EXCEPTION);
		}
		if (certImage.getSize() > MAX_FILE_SIZE) {
			throw new AppException(ErrorCode.FILE_EXTENSION_FAULT);
		}

		userService.uploadCert(universityName, certImage);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/verify/status")
	@Operation(summary = "유저 재학생 인증 상태 조회", description = "유저의 재학생 인증 상태를 확인합니다. (인증 요청 전, 인증 대기 중, 인증 완료, 반려)")
	public ResponseEntity<DataResponse<VerifyStatusResponse>> findVerifyStatus() {
		return ResponseEntity.ok(DataResponse.from(
				VerifyStatusResponse.from(
					userService.findVerifyStatus()
				)
			)
		);
	}
}