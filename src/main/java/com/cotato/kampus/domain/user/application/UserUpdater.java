package com.cotato.kampus.domain.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.user.dao.UserRepository;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class UserUpdater {

	private final ApiUserResolver apiUserResolver;
	private final UserFinder userFinder;
	private final UserRepository userRepository;

	@Transactional
	public Long updateDetails(String nickname, Nationality nationality, PreferredLanguage preferredLanguage) {
		User user = apiUserResolver.getCurrentUser();
		// 유저 세부정보 업데이트 및 UserStatus를 ACTIVE로 변경
		user.updateDetails(nickname, nationality, preferredLanguage);
		return user.getId();
	}

	@Transactional
	public Long updateVerificationStatus(Long userId, Long universityId){
		User user = userFinder.findById(userId);
		user.updateVerificationStatus(universityId);
		return user.getId();
	}

	@Transactional
	public void updateDeviceToken(String deviceToken) {
		User user = apiUserResolver.getCurrentUser();
		user.updateDeviceToken(deviceToken);

		// userRepository.save(user.updateDeviceToken(deviceToken));
	}
}