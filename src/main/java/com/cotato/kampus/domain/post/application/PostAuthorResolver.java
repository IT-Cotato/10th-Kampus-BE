package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.dto.AnonymousOrPostAuthor;
import com.cotato.kampus.domain.post.dto.PostDto;
import com.cotato.kampus.domain.user.dao.UserRepository;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostAuthorResolver {

	private final UserRepository userRepository;
	private final PostFinder postFinder;
	private final ApiUserResolver apiUserResolver;

	public AnonymousOrPostAuthor getAuthor(PostDto postDto) {
		User author = userRepository.findById(postDto.userId())
			.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

		// 현재 사용자가 게시글 작성자인지 확인
		Boolean isAuthor = apiUserResolver.getUserId().equals(author.getId());

		if (postDto.anonymity().equals(Anonymity.ANONYMOUS)) {
			// 익명 사용자 정보 반환
			return AnonymousOrPostAuthor.of(true, isAuthor, -1L, "Anonymous", "Default profile image");
		} else {
			// 작성자 정보 반환
			return AnonymousOrPostAuthor.of(false, isAuthor, author.getId(), author.getUsername(),
				author.getProfileImage());
		}
	}

	public Boolean validatePostAuthor(Long postId, Long userId) {
		PostDto post = postFinder.findPost(postId);

		return post.userId().equals(userId);

	}
}