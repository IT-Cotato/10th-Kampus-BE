package com.cotato.kampus.domain.cert.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Cert {
	private final Long id;
	private final String email;
	private final String univName;
	private final String code;
	private final boolean certified;
	private final Long userId;
	private final LocalDateTime expirationTime;

	@Builder(access = AccessLevel.PRIVATE)
	private Cert(Long id, String email, String univName, String code, boolean certified, Long userId, LocalDateTime expirationTime) {
		this.id = id;
		this.email = email;
		this.univName = univName;
		this.code = code;
		this.certified = certified;
		this.userId = userId;
		this.expirationTime = expirationTime;
	}

	public static Cert create(String email, String univName, String code, boolean certified, Long userId) {
		return Cert.builder()
			.email(email)
			.univName(univName)
			.code(code)
			.certified(certified)
			.userId(userId)
			.expirationTime(LocalDateTime.now().plusMinutes(10)) // 10분간 유효
			.build();
	}

	public static Cert fromEntity(Long id, String email, String univName, String code, boolean certified, Long userId, LocalDateTime expirationTime) {
		return Cert.builder()
			.id(id)
			.email(email)
			.univName(univName)
			.code(code)
			.certified(certified)
			.userId(userId)
			.expirationTime(expirationTime)
			.build();
	}

	public static Cert createCopy(Long id, String email, String univName, String code, boolean certified, Long userId, LocalDateTime expirationTime) {
		return Cert.builder()
			.id(id)
			.email(email)
			.univName(univName)
			.code(code)
			.certified(certified)
			.userId(userId)
			.expirationTime(expirationTime)
			.build();
	}

	public Cert updateCode(String code) {
		return createCopy(this.id, this.email, this.univName, code, this.certified, this.userId, LocalDateTime.now().plusMinutes(10));
	}

	public Cert setCertified() {
		return createCopy(this.id, this.email, this.univName, this.code, true, this.userId, this.expirationTime);
	}

	public void validateExpired() {
		if(LocalDateTime.now().isAfter(this.expirationTime)) {
			throw new AppException(ErrorCode.VERIFICATION_CODE_EXPIRED);
		}
	}

	public void validateNotCertified() {
		if(this.isCertified()) {
			throw new AppException(ErrorCode.ALREADY_VERIFIED);
		}
	}
}
