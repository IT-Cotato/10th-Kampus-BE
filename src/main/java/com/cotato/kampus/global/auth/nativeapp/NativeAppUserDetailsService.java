package com.cotato.kampus.global.auth.nativeapp;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cotato.kampus.domain.user.dao.UserRepository;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class NativeAppUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String uniqueId) throws UsernameNotFoundException {

		User user = userRepository.findByUniqueId(uniqueId)
			.orElseThrow(() -> {
				return new AppException(ErrorCode.USER_NOT_FOUND);
			});
		log.info("PrincipalDetailService::loadUserByUsername Loading user: {}", user.getUniqueId());
		NativeAppDetailsRequest nativeAppDetailsRequest = NativeAppDetailsRequest.from(user);
		return new NativeAppUserDetails(nativeAppDetailsRequest);
	}
}
