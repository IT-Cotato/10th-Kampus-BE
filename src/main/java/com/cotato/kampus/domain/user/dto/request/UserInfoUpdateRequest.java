package com.cotato.kampus.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserInfoUpdateRequest(
	@NotBlank
	@Pattern(regexp = "^[a-z0-9]{5,20}$", message = "닉네임은 5~20자의 영어 소문자(a-z)와 숫자(0-9)만 입력 가능합니다.")
	String nickname,
	@NotNull(message = "선호 언어를 입력해야 합니다.")
	String preferredLanguage
) {
}