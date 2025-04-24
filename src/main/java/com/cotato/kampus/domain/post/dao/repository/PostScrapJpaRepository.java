package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.dao.entity.PostScrapEntity;

public interface PostScrapJpaRepository extends JpaRepository<PostScrapEntity, Long> {

	void deleteAllByPostId(Long postId);

	boolean existsByPostIdAndUserId(Long postId, Long userId);

	Optional<PostScrapEntity> findByPostIdAndUserId(Long postId, Long userId);

	List<Long> findAllPostIdsByUserId(Long userId);
}
