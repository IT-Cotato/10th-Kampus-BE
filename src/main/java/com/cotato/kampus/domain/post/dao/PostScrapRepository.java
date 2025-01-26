package com.cotato.kampus.domain.post.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.domain.PostScrap;

public interface PostScrapRepository extends JpaRepository<PostScrap, Long> {

	boolean existsByUserIdAndPostId(Long userId, Long postId);
}
