package com.cotato.kampus.domain.cert.api.request;

import jakarta.validation.constraints.NotBlank;

public record EmailSendRequest(
	@NotBlank
	String univCode,

	@NotBlank
	String email
) {
}
