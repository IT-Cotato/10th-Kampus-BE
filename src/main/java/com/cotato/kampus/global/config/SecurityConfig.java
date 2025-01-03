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

import com.cotato.deepl.jwt.JwtAuthenticationFilter;
import com.cotato.kampus.global.filter.JWTFilter;
import com.cotato.kampus.global.util.JWTUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityConfig {

	private static final String[] WHITE_LIST = {
		"/v1/api/auth/**",
	};

	private final JWTUtil jwtUtil;
	private static final String LOGIN_URL = "/v1/api/auth/login";

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
			.build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);
		AuthenticationManager authenticationManager = sharedObject.build();
		http.authenticationManager(authenticationManager);

		// 기본 로그인 url 변경
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtUtil);
		jwtAuthenticationFilter.setFilterProcessesUrl(LOGIN_URL);

		//csrf disable
		http
			.csrf((auth) -> auth.disable());

		//From 로그인 방식 disable
		http
			.formLogin((auth) -> auth.disable());

		//http basic 인증 방식 disable
		http
			.httpBasic((auth) -> auth.disable());

		//경로별 인가 작업
		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers(WHITE_LIST).permitAll()
				.requestMatchers("/").hasAnyRole("UNVERIFIED", "ADMIN", "VERIFIED")
				// .requestMatchers("/reissue").permitAll()
				.anyRequest().authenticated());

		//인증 필터 추가
		http
			.addFilterAt(jwtAuthenticationFilter,
				UsernamePasswordAuthenticationFilter.class);

		//jwt 필터 추가(인가)
		http
			.addFilterBefore(new JWTFilter(jwtUtil), JwtAuthenticationFilter.class);

		// http
		// 	.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

		http
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
