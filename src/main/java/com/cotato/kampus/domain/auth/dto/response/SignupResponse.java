package com.cotato.kampus.domain.auth.dto.response;

public record SignupResponse(
	Long userId
) {
	public static SignupResponse of(Long userId) {
		return new SignupResponse(userId);
	}
}
