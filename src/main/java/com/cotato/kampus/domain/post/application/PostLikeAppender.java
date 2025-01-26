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
public class PostLikeAppender {

	private final PostLikeRepository postLikeRepository;

	public void appendPostLike(Long postId, Long userId) {
		postLikeRepository.save(
			PostLike.builder()
				.postId(postId)
				.userId(userId)
				.build()
		);
	}
}