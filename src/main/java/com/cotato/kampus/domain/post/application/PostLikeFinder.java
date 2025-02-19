package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostLikeRepository;

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
}
