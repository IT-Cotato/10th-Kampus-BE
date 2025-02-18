package com.cotato.kampus.domain.post.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.domain.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	Slice<Post> findAllByUserId(Long userId, Pageable pageable);

	Long countByBoardId(Long boardId);

	@Query("SELECT p FROM Post p WHERE p.boardId = :boardId")
	Slice<Post> findAllByBoardId(@Param("boardId") Long boardId, Pageable pageable);
}