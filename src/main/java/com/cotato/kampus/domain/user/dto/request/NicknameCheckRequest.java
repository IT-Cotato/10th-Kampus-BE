package com.cotato.kampus.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record NicknameCheckRequest(
	@NotBlank(message = "닉네임을 입력해주세요.")
	@Pattern(regexp = "^[a-z]{5,20}$", message = "닉네임은 5~20자의 영어 소문자(a-z)만 입력 가능합니다.")
	String nickname
) {
}