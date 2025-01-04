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
public class PrincipalDetails implements UserDetails {

	private final PrincipalDetailsRequest principalDetailsRequest;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(principalDetailsRequest.role().name()));
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return principalDetailsRequest.username();
	}

	public String uniqueId() {
		return principalDetailsRequest.uniqueId();
	}

	public String providerId() {
		return principalDetailsRequest.providerId();
	}
}
