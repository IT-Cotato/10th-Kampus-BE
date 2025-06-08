package com.cotato.kampus.domain.cert.implement;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.cert.domain.Cert;
import com.cotato.kampus.domain.cert.implement.port.CertRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CertFinder {

	private final CertRepository certRepository;

	public Cert findByEmail(String email) {
		return certRepository.findByEmail(email)
			.orElseThrow(() -> new AppException(ErrorCode.CERT_NOT_FOUND));
	}
}
