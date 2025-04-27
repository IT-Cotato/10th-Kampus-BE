package com.cotato.kampus.domain.post.implement.postLike;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.PostLikeRepository;
import com.cotato.kampus.domain.post.domain.PostLike;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeFinder {

	private final PostLikeRepository postLikeRepository;

	public boolean hasUserLikedPost(Long userId, Long postId) {
		return postLikeRepository.existsByPostIdAndUserId(postId, userId);
	}

	public PostLike findPostLikeByPostIdAndUserId(Long postId, Long userId) {
		return postLikeRepository.findByPostIdAndUserId(postId, userId)
			.orElseThrow(() -> new AppException(ErrorCode.POST_LIKE_NOT_FOUND));
	}
}
