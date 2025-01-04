package com.cotato.kampus.global.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class PrincipleDetails implements UserDetails {

	private final PrincipleDetailsRequest principleDetailsRequest;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(principleDetailsRequest.role().name()));
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return principleDetailsRequest.username();
	}

	public String uniqueId() {
		return principleDetailsRequest.uniqueId();
	}

	public String providerId() {
		return principleDetailsRequest.providerId();
	}
}
