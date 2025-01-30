package com.cotato.kampus.domain.user.dto.request;

import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserDetailsUpdateRequest(
	@NotBlank
	@Pattern(regexp = "^[a-z]{5,20}$", message = "닉네임은 5~20자의 영어 소문자(a-z)만 입력 가능합니다.")
	String nickname,
	@NotNull(message = "국적을 입력해야 합니다.")
	Nationality nationality,
	@NotNull(message = "선호 언어를 입력해야 합니다.")
	PreferredLanguage preferredLanguage
) {
}