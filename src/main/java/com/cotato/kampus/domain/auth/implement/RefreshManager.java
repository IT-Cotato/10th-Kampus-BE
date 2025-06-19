package com.cotato.kampus.domain.auth.implement;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.auth.dao.RefreshRepository;
import com.cotato.kampus.domain.auth.domain.RefreshEntity;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class RefreshManager {

	private final RefreshRepository refreshRepository;

	@Transactional
	public void addRefreshEntity(String uniqueId, String username, String refresh, Long expiration) {
		Date date = new Date(System.currentTimeMillis() + expiration);

		if (refreshRepository.existsByUniqueId(uniqueId)) {
			refreshRepository.deleteByUniqueId(uniqueId);
		}
		RefreshEntity refreshEntity = RefreshEntity.builder()
			.uniqueId(uniqueId)
			.username(username)
			.refresh(refresh)
			.expiration(date.toString())
			.build();

		refreshRepository.save(refreshEntity);
	}
}