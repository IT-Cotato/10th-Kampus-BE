package com.cotato.kampus.domain.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.auth.dao.RefreshRepository;
import com.cotato.kampus.domain.auth.domain.ReissuedToken;
import com.cotato.kampus.domain.auth.implement.RefreshManager;
import com.cotato.kampus.global.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RefreshService {

	private final RefreshRepository refreshRepository;
	private final RefreshManager refreshManager;
	private final JwtUtil jwtUtil;

	private static final String TOKEN_PREFIX = "Bearer";
	private static final Long ACCESS_TOKEN_EXP = 604800000L; // 일주일
	private static final Long REFRESH_TOKEN_EXP = 86400000L;

	@Transactional
	public ReissuedToken reissueRefreshToken(final String refresh) {

		// 요청 헤더에서 Authorization 값 추출
		log.info(refresh);

		String uniqueId = jwtUtil.getUniqueId(refresh);
		String username = jwtUtil.getUsername(refresh);
		String role = jwtUtil.getRole(refresh);

		// make new JWT
		String newAccess = jwtUtil.createJwt("access", uniqueId, username, role, ACCESS_TOKEN_EXP);
		String newRefresh = jwtUtil.createJwt("refresh", uniqueId, username, role, REFRESH_TOKEN_EXP);

		// Refresh 토큰에는 Bearer 접두어 X(쿠키에 저장)
		ReissuedToken reissuedToken = ReissuedToken.builder()
			.accessToken(TOKEN_PREFIX + " " + newAccess)
			.refreshToken(newRefresh)
			.build();

		log.info("Access Token : {} ", newAccess);
		log.info("Refresh Token : {} ", newRefresh);

		//Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
		refreshManager.addRefreshEntity(uniqueId, username, newRefresh, REFRESH_TOKEN_EXP);
		return reissuedToken;
	}
}