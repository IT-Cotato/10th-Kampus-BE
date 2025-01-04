package com.cotato.kampus.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class EncodeConfig {

	// @Value("${aes.secret.key}")
	// String aesKey;

	// @Value("${aes.secret.salt}")
	// String salt;

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	// @Bean
	// public AesBytesEncryptor encryptor() {
	// 	return new AesBytesEncryptor(aesKey, salt);
	// }
}

