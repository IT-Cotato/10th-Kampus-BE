package com.cotato.kampus.domain.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.auth.implement.RefreshManager;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.user.application.UserAppender;
import com.cotato.kampus.domain.user.dto.UserDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AuthService {

	private final UserAppender userAppender;
	private final RefreshManager refreshManager;
	private final ApiUserResolver apiUserResolver;

	public Long signup(String email, String uniqueId, String providerId, String username,
		String nickname, String nationality, String languageCode) {
		return userAppender.appendUser(
			email,
			uniqueId,
			providerId,
			username,
			nickname,
			nationality,
			languageCode
		);
	}

	@Transactional
	public void logout() {
		// 액세스 토큰에서 사용자 정보 추출
		UserDto currentUser = apiUserResolver.getCurrentUserDto();

		// DB에서 해당 사용자의 리프레시 토큰 삭제
		refreshManager.deleteRefreshToken(currentUser.uniqueId());
	}
}
