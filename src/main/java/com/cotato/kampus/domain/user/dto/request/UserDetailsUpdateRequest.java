package com.cotato.kampus.domain.user.dto.request;

import com.cotato.kampus.domain.user.enums.Nationality;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserDetailsUpdateRequest(
	@NotBlank
	@Pattern(regexp = "^[a-z0-9]{5,20}$", message = "닉네임은 5~20자의 영어 소문자(a-z)와 숫자(0-9)만 입력 가능합니다.")
	String nickname,

	@NotNull(message = "국적을 입력해야 합니다.")
	Nationality nationality,

	@NotNull(message = "선호 언어를 입력해야 합니다.")
	String preferredLanguage,

	@NotNull(message = "개인정보 수집 및 이용 동의는 필수입니다.")
	@AssertTrue(message = "개인정보 수집 및 이용에 동의해야 합니다.")
	Boolean personalInfoAgreement,

	@NotNull(message = "개인정보 처리방침 동의는 필수입니다.")
	@AssertTrue(message = "개인정보 처리방침에 동의해야 합니다.")
	Boolean privacyPolicyAgreement,

	@NotNull(message = "서비스 이용약관 동의는 필수입니다.")
	@AssertTrue(message = "서비스 이용약관에 동의해야 합니다.")
	Boolean termsOfServiceAgreement,

	@NotNull(message = "마케팅 정보 수신 동의 여부를 입력해야 합니다.")
	Boolean marketingAgreement
) {
}