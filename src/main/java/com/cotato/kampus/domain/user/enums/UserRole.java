package com.cotato.kampus.domain.user.enums;

public enum UserRole {
	ADMIN,
	VERIFIED,
	UNVERIFIED;

	public boolean canAccessUniversityBoard() {
		return this == VERIFIED || this == ADMIN;
	}
}
