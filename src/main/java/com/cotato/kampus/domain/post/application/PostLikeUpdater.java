package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostLikeRepository;
import com.cotato.kampus.domain.post.domain.PostLike;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostLikeUpdater {

	private final PostLikeRepository postLikeRepository;
	private final PostLikeFinder postLikeFinder;

	public void appendPostLike(Long postId, Long userId) {
		postLikeRepository.save(
			PostLike.builder()
				.postId(postId)
				.userId(userId)
				.build()
		);
	}

	public void deletePostLike(Long postId, Long userId) {
		PostLike postLike = postLikeFinder.findPostLikeByUserAndPost(postId, userId);
		postLikeRepository.delete(postLike);
	}
}