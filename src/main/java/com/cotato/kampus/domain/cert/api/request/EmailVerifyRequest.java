package com.cotato.kampus.domain.cert.api.request;

import jakarta.validation.constraints.NotBlank;

public record EmailVerifyRequest(
	@NotBlank
	String email,

	@NotBlank
	String code
) {
}
