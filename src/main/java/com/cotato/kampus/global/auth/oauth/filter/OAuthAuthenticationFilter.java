package com.cotato.kampus.global.auth.oauth.filter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.global.auth.oauth.service.dto.CustomOAuth2User;
import com.cotato.kampus.global.auth.oauth.service.dto.OAuthUserRequest;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.JwtException;
import com.cotato.kampus.global.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuthAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final JwtUtil jwtUtil;
	private final String ACCESS_CATEGORY = "access";

	public OAuthAuthenticationFilter(RequestMatcher matcher, JwtUtil jwtUtil) {
		super(matcher);
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
		throws AuthenticationException {
		// 토큰 추출
		String token = extractToken(request.getHeader(HttpHeaders.AUTHORIZATION));
		log.info("Token extracted from header: {}", token);

		// 토큰 검증
		validateToken(token);

		// 사용자 정보 추출 및 인증 객체 생성
		OAuthUserRequest OAuthUserRequest = createOAuthUserRequest(token);
		CustomOAuth2User customOAuth2User = new CustomOAuth2User(OAuthUserRequest);

		return new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authentication) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authenticationException) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		log.error("Authentication not success");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
	}

	private String extractToken(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new JwtException(ErrorCode.TOKEN_NOT_FOUND);
		}
		return authorizationHeader.substring(7).trim(); // Bearer 제거
	}

	private void validateToken(String token) {
		if (jwtUtil.isExpired(token)) {
			throw new JwtException(ErrorCode.TOKEN_EXPIRED);
		}
		String category = jwtUtil.getCategory(token);
		if (!category.equals(ACCESS_CATEGORY)) {
			throw new JwtException(ErrorCode.INVALID_TOKEN);
		}
	}

	private OAuthUserRequest createOAuthUserRequest(String token) {
		return OAuthUserRequest.builder()
			.uniqueId(jwtUtil.getUniqueId(token))
			.username(jwtUtil.getUsername(token))
			.userRole(UserRole.valueOf(jwtUtil.getRole(token)))
			.build();
	}
}
