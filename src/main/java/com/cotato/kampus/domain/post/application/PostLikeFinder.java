package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostLikeRepository;
import com.cotato.kampus.domain.post.domain.PostLike;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostLikeFinder {

	private final PostLikeRepository postLikeRepository;

	public boolean isPostLikedByUser(Long userId, Long postId) {
		return postLikeRepository.existsByPostIdAndUserId(postId, userId);
	}

	public PostLike findPostLikeByUserAndPost(Long postId, Long userId) {
		return postLikeRepository.findByUserIdAndPostId(userId, postId)
			.orElseThrow(() -> new AppException(ErrorCode.POST_UNLIKE_FORBIDDEN));
	}
}
