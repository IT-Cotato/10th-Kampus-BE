package com.cotato.kampus.domain.comment.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.comment.enums.CommentStatus;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentValidator {

	private final CommentFinder commentFinder;

	public void validateParent(Long postId, Long parentId){

		if (parentId != null) {
			Comment parentComment = commentFinder.findComment(parentId);

			checkParentBelongsToPost(postId, parentComment);
			checkParentDepth(parentComment);
			checkParentStatus(parentComment);
		}
	}

	public void checkParentBelongsToPost(Long postId, Comment parentComment){

		boolean isParentBelongsToPost = parentComment.getPostId().equals(postId);
		if(!isParentBelongsToPost){
			throw new AppException(ErrorCode.INVALID_PARENT_COMMENT);
		}

	}

	public void checkParentDepth(Comment parentComment){

		if(parentComment.getParentId() != null){
			throw new AppException(ErrorCode.INVALID_PARENT_COMMENT);
		}

	}

	public void checkParentStatus(Comment parentComment) {
		// 부모 댓글이 삭제된 경우 대댓글 생성 불가
		if (parentComment.getCommentStatus() != CommentStatus.NORMAL) {
			throw new AppException(ErrorCode.INVALID_PARENT_COMMENT);
		}
	}

	public void validateCommentStatus(Long commentId){

		Comment comment = commentFinder.findComment(commentId);
		if (comment.getCommentStatus() != CommentStatus.NORMAL) {
			throw new AppException(ErrorCode.INVALID_COMMENT);
		}
	}
}
