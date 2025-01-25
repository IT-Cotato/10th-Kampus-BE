package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostLikeRepository;
import com.cotato.kampus.domain.post.dao.PostRepository;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostLikeValidator {

	private final PostRepository postRepository;
	private final PostLikeRepository postLikeRepository;

	public void validatePostLike(Long postId, Long userId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

		validateAuthor(post, userId);
		validateDuplicateLike(postId, userId);
	}

	private void validateAuthor(Post post, Long userId) {
		if (post.getUserId().equals(userId)) {
			throw new AppException(ErrorCode.POST_LIKE_FORBIDDEN);
		}
	}

	private void validateDuplicateLike(Long postId, Long userId) {
		if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
			throw new AppException(ErrorCode.POST_LIKE_DUPLICATED);
		}
	}
}