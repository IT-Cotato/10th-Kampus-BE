package com.cotato.kampus.domain.comment.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.comment.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	Optional<Comment> findFirstByPostIdAndUserId(Long postId, Long userId);

	boolean existsByParentId(Long parentId);

	List<Comment> findAllByPostIdOrderByCreatedTimeAsc(Long postId);

	Slice<Comment> findAllByUserId(Long userId, Pageable pageable);

	@Query("""
		SELECT c.postId 
		FROM Comment c
		WHERE c.userId = :userId
		GROUP BY c.postId
		ORDER BY MAX(c.createdTime) DESC
		""")
	List<Long> findRecentPostIdsByUserId(@Param("userId") Long userId);
}
