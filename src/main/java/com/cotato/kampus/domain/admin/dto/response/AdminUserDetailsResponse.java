package com.cotato.kampus.domain.admin.dto.response;

import com.cotato.kampus.domain.admin.dto.AdminUserInfo;
import com.cotato.kampus.domain.user.enums.UserRole;

public record AdminUserDetailsResponse(
	Long id,
	String nickname,
	String email,
	UserRole role
) {
	public static AdminUserDetailsResponse from(AdminUserInfo adminUserInfo) {
		return new AdminUserDetailsResponse(
			adminUserInfo.id(),
			adminUserInfo.nickname(),
			adminUserInfo.email(),
			adminUserInfo.role()
		);
	}
}