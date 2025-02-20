package com.cotato.kampus.domain.admin.dto.request;

import com.cotato.kampus.domain.user.enums.UserRole;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ChangeUserRoleRequest(
	@NotNull(message = "userId는 필수입니다.")
	Long userId,

	@NotNull(message = "role은 필수입니다.")
	@Pattern(regexp = "^(ADMIN|VERIFIED|UNVERIFIED)$",
		message = "role은 ADMIN, VERIFIED, UNVERIFIED 중 하나여야 합니다.")
	UserRole role
) {
}