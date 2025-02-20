package com.cotato.kampus.domain.admin.dto;

import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;

public record AdminUserInfo(
	Long id,
	String nickname,
	String email,
	UserRole role
) {
	public static AdminUserInfo from(UserDto userDto) {
		return new AdminUserInfo(
			userDto.id(),
			userDto.nickname(),
			userDto.email(),
			userDto.userRole()
		);
	}
}