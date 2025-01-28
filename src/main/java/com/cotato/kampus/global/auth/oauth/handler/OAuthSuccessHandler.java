package com.cotato.kampus.global.auth.oauth.handler;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.cotato.kampus.domain.auth.application.RefreshService;
import com.cotato.kampus.global.auth.oauth.service.dto.CustomOAuth2User;
import com.cotato.kampus.global.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtUtil jwtUtil;
	private final RefreshService refreshService;

	private static final String ACCESS_HEADER_NAME = "Authorization";
	private static final String REFRESH_HEADER_NAME = "Refresh-Token";
	private static final String TOKEN_PREFIX = "Bearer";
	private static final Long ACCESS_TOKEN_EXP = 6000000L;
	private static final Long REFRESH_TOKEN_EXP = 86400000L;
	private static final String REDIRECT_URL = "http://localhost:3000/login";

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {

		CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();

		String uniqueId = customOAuth2User.getUniqueId();
		String username = customOAuth2User.getName();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();

		String access = jwtUtil.createJwt("access", uniqueId, username, role, ACCESS_TOKEN_EXP);
		String refresh = jwtUtil.createJwt("refresh", uniqueId, username, role, REFRESH_TOKEN_EXP);

		refreshService.addRefreshEntity(uniqueId, username, refresh, 86400000L);

		log.info("Access Token : {} ", TOKEN_PREFIX + " " + access);
		log.info("Refresh Token : {} ", TOKEN_PREFIX + " " + refresh);

		// Bearer 토큰을 헤더에 추가
		response.setHeader(ACCESS_HEADER_NAME, TOKEN_PREFIX + access);
		response.setHeader(REFRESH_HEADER_NAME, TOKEN_PREFIX + refresh);
		response.sendRedirect(REDIRECT_URL + "?accessToken=" + access
			+ "&refreshToken=" + refresh);
	}
}