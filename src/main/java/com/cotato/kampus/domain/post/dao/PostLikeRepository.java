package com.cotato.kampus.domain.post.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.domain.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
	Boolean existsByPostIdAndUserId(Long postId, Long userId);
}