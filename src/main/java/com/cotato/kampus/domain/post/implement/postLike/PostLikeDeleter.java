package com.cotato.kampus.domain.post.implement.postLike;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.domain.PostLike;
import com.cotato.kampus.domain.post.implement.port.PostLikeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class PostLikeDeleter {

	private final PostLikeRepository postLikeRepository;

	public void delete(PostLike postLike) {
		postLikeRepository.delete(postLike);
	}

	public void deleteAllByPostId(Long postId) {
		postLikeRepository.deleteAllByPostId(postId);
	}
}
