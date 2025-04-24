package com.cotato.kampus.domain.post.implement.port;

import java.util.Optional;

import com.cotato.kampus.domain.post.domain.PostLike;

public interface PostLikeRepository{

	PostLike save(PostLike postLike);

	void delete(PostLike postLike);

	boolean existsByPostIdAndUserId(Long postId, Long userId);

	Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

	void deleteAllByPostId(Long postId);
}