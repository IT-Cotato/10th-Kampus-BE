package com.cotato.kampus.global.auth.nativeapp;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NativeAppUserDetails implements UserDetails {

	private final NativeAppDetailsRequest request;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(() -> request.role().name());
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return request.username();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getUniqueId() {
		return request.uniqueId();
	}
}
