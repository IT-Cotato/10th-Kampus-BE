package com.cotato.kampus.domain.comment.application;

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
public class CommentLikeFinder {

	private final CommentLikeRepository commentLikeRepository;

	public CommentLike find(Long userId, Long commentId) {
		return commentLikeRepository.findByUserIdAndCommentId(userId, commentId)
			.orElseThrow(() -> new AppException(ErrorCode.COMMENT_UNLIKE_FORBIDDEN));
	}

}
