package com.cotato.kampus.domain.university.application;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.univcert.api.UnivCert;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UnivEmailVerifier {

	@Value("${univcert.api.key}")
	private String apiKey;

	public Map<String, Object> sendMail(String email, String univName) throws IOException {
		UnivCert.clear(apiKey, email);
		return UnivCert.certify(apiKey, email, univName, true);
	}
}