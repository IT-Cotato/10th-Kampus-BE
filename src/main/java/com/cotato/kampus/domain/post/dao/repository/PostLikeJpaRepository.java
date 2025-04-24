package com.cotato.kampus.domain.post.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.dao.entity.PostLikeEntity;

public interface PostLikeJpaRepository extends JpaRepository<PostLikeEntity, Long> {

	boolean existsByPostIdAndUserId(Long postId, Long userId);

	Optional<PostLikeEntity> findByPostIdAndUserId(Long postId, Long userId);

	void deleteAllByPostId(Long postId);
}
