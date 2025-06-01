package com.cotato.kampus.domain.auth.application;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.auth.dao.RefreshRepository;
import com.cotato.kampus.domain.auth.domain.RefreshEntity;
import com.cotato.kampus.domain.auth.domain.ReissuedToken;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.JwtAuthenticationException;
import com.cotato.kampus.global.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RefreshService {

	private final RefreshRepository refreshRepository;
	private final JwtUtil jwtUtil;

	private static final String TOKEN_PREFIX = "Bearer";
	private static final Long ACCESS_TOKEN_EXP = 604800000L; // 일주일
	private static final Long REFRESH_TOKEN_EXP = 86400000L;

	@Transactional
	public ReissuedToken reissueRefreshToken(String refresh) {

		// 요청 헤더에서 Authorization 값 추출
		log.info(refresh);
		if (refresh == null || !refresh.startsWith("Bearer ")) {
			log.error("Authorization header is missing or does not start with Bearer");
			throw new JwtAuthenticationException(ErrorCode.INVALID_TOKEN);
		}

		// "Bearer " 이후의 토큰 값만 추출
		String token = refresh.substring(7).trim();

		String uniqueId = jwtUtil.getUniqueId(token);
		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);

		//make new JWT
		String newAccess = jwtUtil.createJwt("access", uniqueId, username, role, ACCESS_TOKEN_EXP);
		String newRefresh = jwtUtil.createJwt("refresh", uniqueId, username, role, REFRESH_TOKEN_EXP);
		ReissuedToken reissuedToken = ReissuedToken.builder()
			.accessToken(TOKEN_PREFIX + " " + newAccess)
			.refreshToken(TOKEN_PREFIX + " " + newRefresh)
			.build();

		log.info("Access Token : {} ", TOKEN_PREFIX + " " + newAccess);
		log.info("Refresh Token : {} ", TOKEN_PREFIX + " " + newRefresh);

		//Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
		addRefreshEntity(uniqueId, username, newRefresh, REFRESH_TOKEN_EXP);
		return reissuedToken;
	}

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

	public Cookie createCookie(String key, String value) {

		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(24 * 60 * 60);
		//cookie.setSecure(true);
		cookie.setPath("/");  //모든 위치에서 쿠키를 볼 수 있음
		cookie.setHttpOnly(true); //자바스크립트가 쿠키를 가져가지 못하게 함

		return cookie;
	}
}