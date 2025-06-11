package com.cotato.kampus.domain.cert.api.request;

import jakarta.validation.constraints.NotBlank;

public record CertifyRequest(
	@NotBlank
	String univCode,

	@NotBlank
	String email
) {
}
