package com.cotato.kampus.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cotato.kampus.global.auth.SocialAuthenticationProvider;
import com.cotato.kampus.global.auth.filter.JwtAuthenticationFilter;
import com.cotato.kampus.global.auth.filter.JwtAuthorizationFilter;
import com.cotato.kampus.global.util.JWTUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JWTUtil jwtUtil;
	private final SocialAuthenticationProvider socialAuthenticationProvider;
	private static final String LOGIN_URL = "/v1/api/auth/login";
	private static final String[] WHITE_LIST = {
		"/v1/api/auth/signup",
		"/v1/api/products",
		"/v1/api/boards/**"
	};

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
			.authenticationProvider(socialAuthenticationProvider)
			.build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// AuthenticationManager 설정
		AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);
		AuthenticationManager authenticationManager = sharedObject.build();
		http.authenticationManager(authenticationManager);

		// JWT Authentication Filter 설정
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtUtil);
		jwtAuthenticationFilter.setFilterProcessesUrl(LOGIN_URL);

		http
			// CSRF 비활성화
			.csrf(csrf -> csrf.disable())

			// Form 로그인 및 HTTP Basic 비활성화
			.formLogin(form -> form.disable())
			.httpBasic(httpBasic -> httpBasic.disable())

			// 경로별 권한 설정
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(WHITE_LIST).permitAll()
				.requestMatchers("/").hasAnyAuthority("UNVERIFIED", "ADMIN", "VERIFIED")
				.requestMatchers("/v1/api/auth/health").hasAnyAuthority("UNVERIFIED", "ADMIN", "VERIFIED")
				.anyRequest().authenticated()
			)

			// 인증 필터 추가
			.addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

			// JWT Authorization 필터 추가
			.addFilterBefore(new JwtAuthorizationFilter(jwtUtil), JwtAuthenticationFilter.class)

			// 세션 관리 설정 (Stateless)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
