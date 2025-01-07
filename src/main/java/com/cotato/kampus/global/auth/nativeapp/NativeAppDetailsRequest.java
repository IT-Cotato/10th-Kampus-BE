package com.cotato.kampus.global.auth.nativeapp;

import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.UserRole;

public record NativeAppDetailsRequest(
	String username,
	String uniqueId,
	UserRole role
) {

	public static NativeAppDetailsRequest of(String username, String uniqueId, UserRole role) {
		return new NativeAppDetailsRequest(username, uniqueId, role);
	}

	public static NativeAppDetailsRequest from(User user) {
		return new NativeAppDetailsRequest(user.getUsername(), user.getUniqueId(),
			user.getUserRole());
	}
}