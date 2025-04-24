package com.cotato.kampus.domain.post.implement.postLike;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.domain.PostLike;
import com.cotato.kampus.domain.post.implement.port.PostLikeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class PostLikeAppender {

	private final PostLikeRepository postLikeRepository;

	public PostLike append(Long postId, Long userId) {
		PostLike postLike = PostLike.builder()
			.postId(postId)
			.userId(userId)
			.build();

		return postLikeRepository.save(postLike);
	}
}
