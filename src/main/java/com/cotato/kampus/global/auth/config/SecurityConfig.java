package com.cotato.kampus.global.auth.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.cotato.kampus.global.auth.SkipPathRequestMatcher;
import com.cotato.kampus.global.auth.nativeapp.NativeAppAuthProvider;
import com.cotato.kampus.global.auth.nativeapp.filter.JwtAuthenticationFilter;
import com.cotato.kampus.global.auth.nativeapp.filter.NativeAppLoginFilter;
import com.cotato.kampus.global.auth.oauth.handler.OAuthSuccessHandler;
import com.cotato.kampus.global.auth.oauth.service.CustomOAuth2UserService;
import com.cotato.kampus.global.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtUtil jwtUtil;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuthSuccessHandler OAuthSuccessHandler;
	private final NativeAppAuthProvider nativeAppAuthProvider;
	private final CorsConfigurationSource corsConfigurationSource;

	private static final String LOGIN_URL = "/v1/api/auth/login";
	private static final String API_ROOT_URL = "/v1/api/**";
	private static final String[] WHITE_LIST = {
		"/v1/api/auth/signup",
		"/v1/api/products/**",
		"/v1/api/boards/**",
		"/v1/api/users/**",
		"/v1/api/admin/**",
		"/websocket/**",
		"/v1/api/chats/**",
		"/swagger-ui/**",
		"/v3/api-docs/**"
	};

	// jwtAuthenticationFilter에서 스킵하는 url
	private static final String[] JWT_SKIP_URL = {
		"/v1/api/auth/login",
		"/v1/api/auth/signup"
	};

	// nativeAppLoginFilter > nativeAppAuthFilter
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource)) // ✅ CorsConfig 설정 적용
			.csrf(csrf -> csrf.disable()) // CSRF 비활성화
			.formLogin(form -> form.disable()) // Form 로그인 비활성화
			.httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 비활성화

			.authenticationManager(authenticationManager(http)) // AuthenticationManager 설정

			.addFilterAt(nativeAppLoginFilter(http), UsernamePasswordAuthenticationFilter.class)
			.addFilterAfter(jwtAuthenticationFilter(), NativeAppLoginFilter.class)
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
				.successHandler(OAuthSuccessHandler)
			)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(WHITE_LIST).permitAll()
				.requestMatchers("/v1/api/auth/health").hasAnyAuthority("UNVERIFIED", "ADMIN", "VERIFIED")
				.anyRequest().authenticated()
			)
			.sessionManagement(
				session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Stateless 세션

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
			.authenticationProvider(nativeAppAuthProvider)
			.build();
	}

	private NativeAppLoginFilter nativeAppLoginFilter(HttpSecurity http) throws Exception {
		NativeAppLoginFilter filter = new NativeAppLoginFilter(authenticationManager(http), jwtUtil);
		filter.setFilterProcessesUrl(LOGIN_URL);
		return filter;
	}

	private JwtAuthenticationFilter jwtAuthenticationFilter() {
		var matcher = new SkipPathRequestMatcher(List.of(JWT_SKIP_URL), API_ROOT_URL);  // API_ROOT_URL로 오는 요청만 처리
		return new JwtAuthenticationFilter(matcher, jwtUtil);
	}
}
