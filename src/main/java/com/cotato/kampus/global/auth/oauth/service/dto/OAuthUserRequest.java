package com.cotato.kampus.global.auth.oauth.service.dto;

import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.domain.user.enums.UserStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthUserRequest {
	private String uniqueId;
	private String username;
	private String email;
	private UserRole userRole;
	private UserStatus userStatus;

	public OAuthUserRequest(User user) {
		this.uniqueId = user.getUniqueId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.userRole = user.getUserRole();
		this.userStatus = user.getUserStatus();
	}

	@Builder
	public OAuthUserRequest(String uniqueId, String username, UserRole userRole, UserStatus userStatus) {
		this.uniqueId = uniqueId;
		this.username = username;
		this.userRole = userRole;
		this.userStatus = userStatus;
	}
}