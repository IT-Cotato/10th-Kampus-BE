package com.cotato.kampus.global.auth.nativeapp.filter;

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
import com.cotato.kampus.global.auth.nativeapp.NativeAppUserDetails;
import com.cotato.kampus.global.auth.nativeapp.NativeAppDetailsRequest;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.JwtException;
import com.cotato.kampus.global.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NativeAppAuthFilter extends AbstractAuthenticationProcessingFilter {

	private final JwtUtil jwtUtil;

	public NativeAppAuthFilter(RequestMatcher matcher, JwtUtil jwtUtil) {
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
		NativeAppDetailsRequest detailsRequest = createPrincipalDetailsRequest(token);
		NativeAppUserDetails nativeAppUserDetails = new NativeAppUserDetails(detailsRequest);

		return new UsernamePasswordAuthenticationToken(nativeAppUserDetails, null, nativeAppUserDetails.getAuthorities());
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
		log.error("Authentication not successful: {}", authenticationException.getMessage());
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
	}

	private NativeAppDetailsRequest createPrincipalDetailsRequest(String token) {
		return NativeAppDetailsRequest.of(
			jwtUtil.getUniqueId(token),
			jwtUtil.getUsername(token),
			UserRole.valueOf(jwtUtil.getRole(token))
		);
	}
}
