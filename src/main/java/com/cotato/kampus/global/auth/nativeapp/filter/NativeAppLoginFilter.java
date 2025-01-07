package com.cotato.kampus.global.auth.nativeapp.filter;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.global.auth.nativeapp.NativeAppUserDetails;
import com.cotato.kampus.global.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NativeAppLoginFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24; // 24시간
	private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24; // 24시간
	private static final String ACCESS_HEADER_NAME = "Authorization";
	private static final String REFRESH_HEADER_NAME = "Refresh-Token";
	private final String ACCESS_CATEGORY = "access";
	private final String REFRESH_CATEGORY = "refresh";
	private static final String TOKEN_PREFIX = "Bearer ";

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			User user = mapper.readValue(request.getInputStream(), User.class);
			log.info("JwtAuthenticationFilter::attemptAuthentication user uniqueId: {}", user.getUniqueId());
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				user.getUniqueId(),
				user.getProviderId()
			);
			return authenticationManager.authenticate(authenticationToken);
		} catch (Exception e) {
			log.error("JwtAuthenticationFilter::attemptAuthentication Exception occur: {}", e.getMessage());
			throw new AuthenticationException("로그인 시도에 실패했습니다.") {
			};
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authentication) {
		NativeAppUserDetails nativeAppUserDetails = (NativeAppUserDetails)authentication.getPrincipal();

		String uniqueId = nativeAppUserDetails.getUniqueId();
		String username = nativeAppUserDetails.getUsername();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();

		String access = jwtUtil.createJwt(ACCESS_CATEGORY, uniqueId, username, role, ACCESS_TOKEN_EXPIRATION_TIME);
		String refresh = jwtUtil.createJwt(REFRESH_CATEGORY, uniqueId, username, role, REFRESH_TOKEN_EXPIRATION_TIME);

		response.addHeader(ACCESS_HEADER_NAME, TOKEN_PREFIX + access);
		response.addHeader(REFRESH_HEADER_NAME, TOKEN_PREFIX + refresh);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) {
		response.setStatus(401);
	}
}