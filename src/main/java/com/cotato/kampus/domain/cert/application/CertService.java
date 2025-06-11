package com.cotato.kampus.domain.cert.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cotato.kampus.domain.cert.enums.UnivMail;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertService {

	public boolean checkUnivCode(String univCode) {
		return UnivMail.exists(univCode);
	}
}
