package com.cotato.kampus.domain.user.dto.response;

public record NicknameCheckResponse(
	Boolean isAvailable
) {
	public static NicknameCheckResponse from(Boolean isAvailable) {
		return new NicknameCheckResponse(isAvailable);
	}
}