package com.cotato.kampus.domain.user.dto.request;

public record SendMailRequest(
	String email,
	Long universityId
) {
}
