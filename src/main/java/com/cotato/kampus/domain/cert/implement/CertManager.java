package com.cotato.kampus.domain.cert.implement;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.cert.domain.Cert;
import com.cotato.kampus.domain.cert.implement.port.CertRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CertManager {

	private final CertRepository certRepository;

	@Transactional
	public Cert append(String email, String univCode, String code, Long userId) {
		Cert cert = Cert.create(email, univCode, code, false, userId);
		return certRepository.save(cert);
	}

	@Transactional
	public void updateCodeAndExpiration(Cert cert, String code) {
		Cert updatedCert = cert.updateCode(code);
		certRepository.save(updatedCert);
	}

	@Transactional
	public void certify(Cert cert) {
		Cert updatedCert = cert.setCertified();
		certRepository.save(updatedCert);
	}
}
