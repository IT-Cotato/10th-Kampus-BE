package com.cotato.kampus.domain.user.dto.request;

public record ConfirmMailRequest(
	String email,
	String univName,
	int code
) {
}
