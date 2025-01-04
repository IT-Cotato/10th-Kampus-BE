package com.cotato.kampus.domain.common.application;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.cotato.kampus.domain.user.application.UserFinder;
import com.cotato.kampus.global.auth.PrincipalDetails;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiUserResolver {

	private final UserFinder userFinder;

	public Long getUserId() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails userDetails = (PrincipalDetails)principal;

		return userFinder.findByUniqueId(userDetails.uniqueId()).getId();
	}
}
