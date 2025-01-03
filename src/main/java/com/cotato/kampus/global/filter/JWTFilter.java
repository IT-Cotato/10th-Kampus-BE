package com.cotato.kampus.global.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.global.auth.PrincipleDetails;
import com.cotato.kampus.global.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		//request에서 Authorization 헤더를 찾음
		String authorization = request.getHeader("Authorization");

		//Authorization 헤더 검증
		if (authorization == null || !authorization.startsWith("Bearer ")) {

			log.info("token null");
			filterChain.doFilter(request, response);

			//조건이 해당되면 메소드 종료 (필수)
			return;
		}

		String token = authorization.split(" ")[1];

		//토큰 소멸 시간 검증
		if (jwtUtil.isExpired(token)) {

			log.info("token expired");
			filterChain.doFilter(request, response);

			//조건이 해당되면 메소드 종료 (필수)
			return;
		}

		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);

		log.info("username: {}, role {}", username, role);

		User user = User.builder()
			.username(username)
			.email("tempemail")
			.password("temppassword")
			.userRole(UserRole.valueOf(role))
			.build();

		PrincipleDetails principleDetails = new PrincipleDetails(user);

		Authentication authToken = new UsernamePasswordAuthenticationToken(
			principleDetails,
			null,
			principleDetails.getAuthorities()
		);

		SecurityContextHolder.getContext().setAuthentication(authToken);
		log.info("JWTFilter: SecurityContext Authentication: {}",
			SecurityContextHolder.getContext().getAuthentication());

		filterChain.doFilter(request, response);
	}
}
