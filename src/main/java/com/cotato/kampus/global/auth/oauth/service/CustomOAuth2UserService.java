package com.cotato.kampus.global.auth.oauth.service;

import com.cotato.kampus.domain.board.application.BoardFavoriteService;
import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.board.implement.boardFavorite.BoardFavoriteManager;
import com.cotato.kampus.domain.user.dao.UserRepository;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.global.auth.oauth.service.dto.CustomOAuth2User;
import com.cotato.kampus.global.auth.oauth.service.dto.OAuth2Attribute;
import com.cotato.kampus.global.auth.oauth.service.dto.OAuthUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;
	private final BoardFinder boardFinder;
	private final BoardFavoriteManager boardFavoriteManager;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2User oAuth2User = super.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId(); //naver, google
		OAuth2Attribute attribute = OAuth2Attribute.of(registrationId, oAuth2User.getAttributes());

		String uniqueId = registrationId + " " + attribute.getProviderId();

		User user = saveOrUpdate(attribute, uniqueId);
		OAuthUserRequest OAuthUserRequest = new OAuthUserRequest(user);

		return new CustomOAuth2User(OAuthUserRequest);
	}

		User saveOrUpdate(OAuth2Attribute attribute, String uniqueId) {
		Optional<User> optionalUser = userRepository.findByUniqueId(uniqueId);

		User user;
		if (optionalUser.isPresent()) {
			user = optionalUser.get().update(attribute.getEmail(), attribute.getUsername());
		} else {
			user = attribute.toEntity(uniqueId);

			List<Long> defaultFavoriteBoardIds = boardFinder.findDefaultFavoriteBoardIds();
			boardFavoriteManager.appendAll(user.getId(), defaultFavoriteBoardIds);
		}
		return userRepository.save(user);
	}
}

