package com.cotato.kampus.domain.auth.dto.request;

public record SignupRequest(
	String email,
	String username,
	String nickname,
	String password,
	String nationality
) {
}
