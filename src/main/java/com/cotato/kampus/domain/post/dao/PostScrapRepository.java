package com.cotato.kampus.domain.post.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.domain.PostScrap;

public interface PostScrapRepository extends JpaRepository<PostScrap, Long> {

	boolean existsByUserIdAndPostId(Long userId, Long postId);

	Optional<PostScrap> findByUserIdAndPostId(Long userId, Long postId);
}
