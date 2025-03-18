package com.cotato.kampus.domain.university.application;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.UnivCertException;
import com.univcert.api.UnivCert;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UnivEmailVerifier {

	private final UnivFinder univFinder;

	@Value("${univcert.api.key}")
	private String apiKey;

	public Map<String, Object> sendMail(String email, String universityCode) throws IOException {
		String universityName = univFinder.findNameByCode(universityCode);

		UnivCert.clear(apiKey, email);
		Map<String, Object> response = UnivCert.certify(apiKey, email, universityName, true);

		validateResponse(response);

		return response;
	}

	public Map<String, Object> verifyCode(String email, String universityCode, int code) throws IOException {
		String universityName = univFinder.findNameByCode(universityCode);
		Map<String, Object> response = UnivCert.certifyCode(apiKey, email, universityName, code);

		validateResponse(response);

		return response;
	}

	private void validateResponse(Map<String, Object> response) {
		if (!(boolean)response.get("success")) {
			String errorMessage = response.get("message").toString();
			throw new UnivCertException(ErrorCode.UNIVCERT_ERROR, errorMessage);
		}
	}
}