package com.cotato.kampus.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cotato.kampus.global.error.ErrorCode;
import com.deepl.api.DeepLException;
import com.deepl.api.Translator;

@Configuration
public class DeepLConfig {

	@Value("${deepl.auth-key}")
	private String authKey;

	@Bean
	public Translator translator() throws DeepLException {
		if (authKey == null || authKey.isEmpty()) {
			throw new DeepLException(ErrorCode.INVALID_DEEPL_AUTH_KEY.getMessage());
		}
		return new Translator(authKey);
	}
}
