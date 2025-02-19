package com.cotato.kampus.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

	private static final String[] ORIGINS = {
		"http://localhost:3000",
		"http://localhost:5173",
		"http://localhost:8000",
		"http://54.180.123.60:8080",
		"https://kampus.kro.kr",
		"https://dc92clgvselcq.cloudfront.net",
		"https://kampusapp.kro.kr"
	};

	private static final String[] ALLOWED_HEADERS = {
		"Authorization",
		"Refresh-Token"
	};

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		config.setAllowCredentials(true);
		config.setAllowedOrigins(List.of(ORIGINS));
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.setExposedHeaders(List.of(ALLOWED_HEADERS));
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
