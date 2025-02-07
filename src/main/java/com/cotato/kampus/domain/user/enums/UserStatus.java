package com.cotato.kampus.domain.user.enums;

public enum UserStatus {
	PENDING_DETAILS,    // 초기 소셜 로그인 후, 추가 정보 입력 필요
	ACTIVE,             // 모든 필요 정보 입력 완료
	INACTIVE,           // 비활성화된 계정
	BANNED              // 차단된 계정
}