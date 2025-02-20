package com.cotato.kampus.domain.comment.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dao.CommentRepository;
import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.comment.enums.CommentStatus;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDeleter {

	private final CommentRepository commentRepository;
	private final CommentFinder commentFinder;

	@Transactional
	public void delete(Long commentId) {
		// 대댓글이 있으면 삭제된 상태로 업데이트
		if (commentRepository.existsByParentId(commentId)) {
			Comment comment = commentFinder.findComment(commentId);
			comment.setCommentStatus(CommentStatus.DELETED_BY_USER);
		}

		// 댓글 삭제
		commentRepository.deleteById(commentId);
	}
}
