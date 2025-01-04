package com.cotato.kampus.global.auth;

import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.UserRole;

public record PrincipleDetailsRequest(
	String username,
	String uniqueId,
	String providerId,
	UserRole role
) {

	public static PrincipleDetailsRequest of(String username, String uniqueId, String providerId, UserRole role) {
		return new PrincipleDetailsRequest(username, uniqueId, providerId, role);
	}

	public static PrincipleDetailsRequest from(User user) {
		return new PrincipleDetailsRequest(user.getUsername(), user.getUniqueId(), user.getProviderId(),
			user.getUserRole());
	}
}
