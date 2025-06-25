package com.cotato.kampus.domain.cert.domain;

import java.time.LocalDateTime;

public class TestCertHelper {

	public static Cert createCert(
		Long id,
		String email,
		String univCode,
		String code,
		boolean certified,
		Long userId
	) {
		return new Cert(
			id,
			email,
			univCode,
			code,
			certified,
			userId,
			LocalDateTime.now().plusMinutes(10)
		);
	}
}
