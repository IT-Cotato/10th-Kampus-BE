package com.cotato.kampus.global.auth;

import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.UserRole;

public record PrincipalDetailsRequest(
	String username,
	String uniqueId,
	String providerId,
	UserRole role
) {

	public static PrincipalDetailsRequest of(String username, String uniqueId, String providerId, UserRole role) {
		return new PrincipalDetailsRequest(username, uniqueId, providerId, role);
	}

	public static PrincipalDetailsRequest from(User user) {
		return new PrincipalDetailsRequest(user.getUsername(), user.getUniqueId(), user.getProviderId(),
			user.getUserRole());
	}
}
