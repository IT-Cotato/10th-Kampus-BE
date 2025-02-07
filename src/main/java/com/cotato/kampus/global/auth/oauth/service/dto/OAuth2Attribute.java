package com.cotato.kampus.global.auth.oauth.service.dto;

import java.util.Map;

import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.domain.user.enums.UserStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuth2Attribute {

	private Map<String, Object> attributes;
	private String username;
	private String email;
	private String providerId;

	public static OAuth2Attribute of(String registrationId, Map<String, Object> attributes) {
		if (registrationId.equals("google")) {
			return ofGoogle(attributes);
		} else if (registrationId.equals("kakao")) {
			return ofKakao(attributes);
		}
		return ofNaver(attributes);
	}

	private static OAuth2Attribute ofGoogle(Map<String, Object> attributes) {
		return OAuth2Attribute.builder()
			.username(attributes.get("name").toString())
			.email(attributes.get("email").toString())
			.providerId(attributes.get("sub").toString())
			.attributes(attributes)
			.build();
	}

	private static OAuth2Attribute ofKakao(Map<String, Object> attributes) {
		Map<String, Object> kakao_account = (Map<String, Object>)attributes.get(
			"kakao_account");  // 카카오로 받은 데이터에서 계정 정보가 담긴 kakao_account 값을 꺼낸다.
		Map<String, Object> profile = (Map<String, Object>)kakao_account.get(
			"profile");   // 마찬가지로 profile(nickname, image_url.. 등) 정보가 담긴 값을 꺼낸다.

		return OAuth2Attribute.builder()
			.username(profile.get("nickname").toString())
			.email(kakao_account.get("email").toString())
			.providerId(attributes.get("id").toString())
			.attributes(attributes)
			.build();
	}

	private static OAuth2Attribute ofNaver(Map<String, Object> attributes) {
		Map<String, Object> attributesMap = (Map<String, Object>)attributes.get("response");

		return OAuth2Attribute.builder()
			.username(attributesMap.get("name").toString())
			.email(attributesMap.get("email").toString())
			.providerId(attributesMap.get("id").toString())
			.attributes(attributesMap)
			.build();
	}

	public User toEntity(String uniqueId) {
		return User.builder()
			.email(email)
			.uniqueId(uniqueId)
			.providerId(providerId)
			.username(username)
			.nickname("nickname")
			.nationality(Nationality.OTHER)
			.preferredLanguage(PreferredLanguage.ENGLISH_AMERICAN)
			.userRole(UserRole.UNVERIFIED)
			.userStatus(UserStatus.PENDING_DETAILS)
			.build();
	}

}