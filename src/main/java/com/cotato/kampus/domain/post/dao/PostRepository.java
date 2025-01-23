package com.cotato.kampus.domain.post.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.domain.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	Slice<Post> findAllByBoardIdOrderByCreatedTimeDesc(Long boardId, Pageable pageable);

	Slice<Post> findAllByUserId(Long userId, Pageable pageable);
}