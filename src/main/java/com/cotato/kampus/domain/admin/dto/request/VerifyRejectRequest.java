package com.cotato.kampus.domain.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record VerifyRejectRequest(
	@NotBlank
	String rejectionReason
) {
}
