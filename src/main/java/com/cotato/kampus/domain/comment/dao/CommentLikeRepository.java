package com.cotato.kampus.domain.comment.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cotato.kampus.domain.comment.domain.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

	boolean existsByUserIdAndCommentId(Long userId, Long commentId);

	Optional<CommentLike> findByUserIdAndCommentId(Long userId, Long commentId);

	List<CommentLike> findAllByCommentId(Long commentId);

	@Query("SELECT cl.commentId FROM CommentLike cl WHERE cl.userId = :userId AND cl.commentId IN :commentIds")
	List<Long> findCommentIdsByUserIdAndCommentIdIn(@Param("userId") Long userId, @Param("commentIds") List<Long> commentIds);
}
