package com.cotato.kampus.domain.comment.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.comment.enums.CommentStatus;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	Optional<Comment> findFirstByPostIdAndUserId(Long postId, Long userId);

	boolean existsByParentIdAndCommentStatusIn(Long commentId, Set<CommentStatus> commentStatuses);

	List<Comment> findAllByPostIdOrderByCreatedTimeAsc(Long postId);

	Slice<Comment> findAllByUserId(Long userId, Pageable pageable);

}
