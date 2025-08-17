package com.cotato.kampus.domain.comment.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dao.CommentRepository;
import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.comment.enums.CommentStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDeleter {

	private final CommentRepository commentRepository;
	private final CommentFinder commentFinder;
	private final CommentLikeDeleter commentLikeDeleter;

	@Transactional
	public void delete(Long commentId) {
		// 삭제된 상태로 업데이트
		Comment comment = commentFinder.findComment(commentId);
		comment.setCommentStatus(CommentStatus.DELETED_BY_USER);
	}

	@Transactional
	public void deleteAllByPostId(Long postId) {
		List<Comment> comments = commentFinder.findAllByPostId(postId);

		// CommentLike 삭제
		comments.stream()
			.map(Comment::getId)
			.forEach(commentId -> commentLikeDeleter.deleteAllByCommentId(commentId));

		commentRepository.deleteAll(comments);
	}
}
