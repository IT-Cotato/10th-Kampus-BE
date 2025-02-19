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

	Slice<Post> findAllByBoardIdOrderByCreatedTimeDesc(Long boardId, Pageable pageable);

	Slice<Post> findAllByBoardIdOrderByCreatedTimeAsc(Long boardId, Pageable pageable);

	Slice<Post> findAllByBoardIdOrderByLikesDescCreatedTimeDesc(Long boardId, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% ORDER BY p.createdTime DESC")
	Slice<Post> searchAll(@Param("keyword") String keyword, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE (p.boardId = :boardId) AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%) ORDER BY p.createdTime DESC")
	Slice<Post> searchAllByBoardId(@Param("keyword") String keyword, @Param("boardId") Long boardId, Pageable pageable);
}