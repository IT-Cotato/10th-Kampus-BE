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

	/**
	 * 특정 사용자가 좋아요한 댓글 ID 목록을 배치로 조회
	 * N+1 문제 해결을 위한 배치 조회 메서드
	 */
	@Query("SELECT cl.commentId FROM CommentLike cl WHERE cl.userId = :userId AND cl.commentId IN :commentIds")
	List<Long> findCommentIdsByUserIdAndCommentIdIn(@Param("userId") Long userId, @Param("commentIds") List<Long> commentIds);
}
