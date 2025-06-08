package com.cotato.kampus.domain.cert.domain;

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

	@Builder(access = AccessLevel.PRIVATE)
	private Cert(Long id, String email, String univName, String code, boolean certified, Long userId) {
		this.id = id;
		this.email = email;
		this.univName = univName;
		this.code = code;
		this.certified = certified;
		this.userId = userId;
	}

	public static Cert create(String email, String univName, String code, boolean certified, Long userId) {
		return Cert.builder()
			.email(email)
			.univName(univName)
			.code(code)
			.certified(certified)
			.userId(userId)
			.build();
	}

	public static Cert fromEntity(Long id, String email, String univName, String code, boolean certified, Long userId) {
		return Cert.builder()
			.id(id)
			.email(email)
			.univName(univName)
			.code(code)
			.certified(certified)
			.userId(userId)
			.build();
	}

	public static Cert createCopy(Long id, String email, String univName, String code, boolean certified, Long userId) {
		return Cert.builder()
			.id(id)
			.email(email)
			.univName(univName)
			.code(code)
			.certified(certified)
			.userId(userId)
			.build();
	}

	public Cert updateCode(String code) {
		return createCopy(this.id, this.email, this.univName, code, this.certified, this.userId);
	}

	public Cert setCertified() {
		return createCopy(this.id, this.email, this.univName, this.code, true, this.userId);
	}
}
