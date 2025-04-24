package com.cotato.kampus.domain.post.dao.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.dao.entity.PostScrapEntity;

public interface PostScrapJpaRepository extends JpaRepository<PostScrapEntity, Long> {

	void deleteAllByPostId(Long postId);

	boolean existsByPostIdAndUserId(Long postId, Long userId);

	Optional<PostScrapEntity> findByPostIdAndUserId(Long postId, Long userId);

	Slice<PostScrapEntity> findAllByUserId(Long userId, Pageable pageable);
}
