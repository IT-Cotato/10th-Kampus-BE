package com.cotato.kampus.domain.comment.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dao.CommentLikeRepository;
import com.cotato.kampus.domain.comment.domain.CommentLike;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikeDeleter {

	private final CommentLikeRepository commentLikeRepository;

	@Transactional
	public void deleteAllByCommentId(Long commentId) {
		List<CommentLike> commentLikes = commentLikeRepository.findAllByCommentId(commentId);
		commentLikeRepository.deleteAll(commentLikes);
	}

	@Transactional
	public void delete(Long userId, Long commentId) {
		CommentLike commentLike = commentLikeRepository.findByUserIdAndCommentId(userId, commentId)
			.orElseThrow(() -> new AppException(ErrorCode.COMMENT_UNLIKE_FORBIDDEN));
		commentLikeRepository.delete(commentLike);
	}
}
