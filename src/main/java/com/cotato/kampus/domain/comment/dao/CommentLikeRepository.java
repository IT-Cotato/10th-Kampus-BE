package com.cotato.kampus.domain.comment.dao;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.comment.domain.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

	boolean existsByUserIdAndCommentId(Long userId, Long commentId);

	Optional<CommentLike> findByUserIdAndCommentId(Long userId, Long commentId);
}
