package com.cotato.kampus.domain.university.application;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
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

	public void verifyCode(String email, String univName, int code) throws IOException {
		Map<String, Object> response = UnivCert.certifyCode(apiKey, email, univName,  code);

		boolean success = (boolean) response.get("success");
		if(!success){
			throw new AppException(ErrorCode.INVALID_CODE);
		}
	}
}