package com.cotato.kampus.domain.user.enums;

public enum VerificationStatus {
	NOT_REQUESTED, // 인증 요청 전
	PENDING,       // 인증 대기 중
	APPROVED,      // 인증 완료
	REJECTED       // 인증 거절됨
}