package com.cotato.kampus.global.auth.nativeapp;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.cotato.kampus.domain.user.dao.UserRepository;
import com.cotato.kampus.domain.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
NativeAppLoginFilter의 AuthenticationManager의 AuthenticationProvider
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class NativeAppAuthProvider implements AuthenticationProvider {

	private final UserRepository userRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String uniqueId = (String)authentication.getPrincipal();
		log.info("authenticate authentication principal: {}",
			authentication.getPrincipal());

		// 사용자 조회
		User user = userRepository.findByUniqueId(uniqueId)
			.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + uniqueId));

		log.info("authenticate user uniqueId: {}", user.getUniqueId());

		AppUserDetailsRequest appUserDetailsRequest = AppUserDetailsRequest.from(user);

		// 인증 완료된 Authentication 반환
		return new UsernamePasswordAuthenticationToken(
			new AppUserDetails(appUserDetailsRequest), // 인증된 사용자 정보
			null,
			new AppUserDetails(appUserDetailsRequest).getAuthorities()
		);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
