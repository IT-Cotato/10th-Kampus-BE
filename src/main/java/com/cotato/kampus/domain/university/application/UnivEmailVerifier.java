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

	private final UnivFinder univFinder;

	@Value("${univcert.api.key}")
	private String apiKey;

	public Map<String, Object> sendMail(String email, String universityCode) throws IOException {
		String universityName = univFinder.findNameByCode(universityCode);

		UnivCert.clear(apiKey, email);
		return UnivCert.certify(apiKey, email, universityName, true);
	}

	public void verifyCode(String email, String universityCode, int code) throws IOException {
		String universityName = univFinder.findNameByCode(universityCode);
		Map<String, Object> response = UnivCert.certifyCode(apiKey, email, universityName,  code);

		boolean success = (boolean) response.get("success");
		if(!success){
			throw new AppException(ErrorCode.INVALID_CODE);
		}
	}
}