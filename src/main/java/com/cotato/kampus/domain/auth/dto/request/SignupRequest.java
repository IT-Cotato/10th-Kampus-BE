package com.cotato.kampus.domain.auth.dto.request;

public record SignupRequest(
	String email,
	String uniqueId,
	String providerId,
	String username,
	String nickname,
	String nationality,
	String languageCode
) {
}
