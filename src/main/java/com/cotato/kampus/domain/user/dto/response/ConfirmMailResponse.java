package com.cotato.kampus.domain.user.dto.response;

public record ConfirmMailResponse(
	Long userId
) {
	public static ConfirmMailResponse from(Long userId) {
		return new ConfirmMailResponse(userId);
	}
}
