package com.cotato.kampus.domain.comment.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.common.enums.Anonymity;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	Optional<Comment> findFirstByPostIdAndUserIdAndAnonymity(Long postId, Long userId, Anonymity anonymity);

	boolean existsByParentId(Long parentId);

	List<Comment> findAllByPostId(Long postId);

	List<Comment> findAllByParentId(Long parentId);

	Slice<Comment> findAllByUserId(Long userId, Pageable pageable);
}
