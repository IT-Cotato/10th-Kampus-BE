package com.cotato.kampus.global.config;

import java.util.TimeZone;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
class GlobalConfig {

	@PostConstruct
	public void setTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
}