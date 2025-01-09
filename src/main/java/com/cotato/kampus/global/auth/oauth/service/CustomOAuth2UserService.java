package com.cotato.kampus.global.auth.oauth.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cotato.kampus.domain.user.dao.UserRepository;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.global.auth.oauth.service.dto.CustomOAuth2User;
import com.cotato.kampus.global.auth.oauth.service.dto.OAuth2Attribute;
import com.cotato.kampus.global.auth.oauth.service.dto.OAuthUserRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2User oAuth2User = super.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId(); //naver, google
		OAuth2Attribute attribute = OAuth2Attribute.of(registrationId, oAuth2User.getAttributes());

		String uniqueId = registrationId + " " + attribute.getProviderId();

		User user = saveOrUpdate(attribute, uniqueId);
		OAuthUserRequest OAuthUserRequest = new OAuthUserRequest(user);

		return new CustomOAuth2User(OAuthUserRequest);
	}

	private User saveOrUpdate(OAuth2Attribute attribute, String uniqueId) {
		User user = userRepository.findByUniqueId(uniqueId)
			.map(entity -> entity.update(attribute.getEmail(), attribute.getUsername()))
			.orElse(attribute.toEntity(uniqueId));
		return userRepository.save(user);
	}
}

