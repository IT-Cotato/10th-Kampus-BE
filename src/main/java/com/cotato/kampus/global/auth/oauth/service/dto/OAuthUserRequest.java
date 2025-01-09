package com.cotato.kampus.global.auth.oauth.service.dto;

import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.UserRole;

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

	public OAuthUserRequest(User user) {
		this.uniqueId = user.getUniqueId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.userRole = user.getUserRole();
	}

	@Builder
	public OAuthUserRequest(String uniqueId, String username, UserRole userRole) {
		this.uniqueId = uniqueId;
		this.username = username;
		this.userRole = userRole;
	}

}