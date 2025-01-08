package com.cotato.kampus.global.auth.nativeapp;

import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.UserRole;

public record AppUserDetailsRequest(
	String username,
	String uniqueId,
	UserRole role
) {

	public static AppUserDetailsRequest of(String username, String uniqueId, UserRole role) {
		return new AppUserDetailsRequest(username, uniqueId, role);
	}

	public static AppUserDetailsRequest from(User user) {
		return new AppUserDetailsRequest(user.getUsername(), user.getUniqueId(),
			user.getUserRole());
	}
}