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
	private final ApiUserResolver apiUserResolver;
	private final CommentFinder commentFinder;

	public Long delete(Long commentId) {

		User user = apiUserResolver.getCurrentUser();
		Comment comment = commentFinder.findComment(commentId);

		// 작성자 검증
		if(comment.getUserId() != user.getId()){
			throw new AppException(ErrorCode.COMMENT_NOT_AUTHOR);
		}

		// 대댓글이 있으면 삭제된 상태로 업데이트
		if(commentRepository.existsByParentId(commentId)){
			comment.setCommentStatus(CommentStatus.DELETED_BY_USER);
			return commentRepository.save(comment).getId();
		}

		// 댓글 삭제
		commentRepository.delete(comment);
		return commentId;
	}
}
