package com.cotato.kampus.domain.comment.application;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.user.application.UserValidator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentService {

	private final UserValidator userValidator;
	private final CommentAppender commentAppender;
	private final CommentValidator commentValidator;
	private final CommentDeleter commentDeleter;
	private final AnonymousNumberAllocator anonymousNumberAllocator;
	private final CommentLikeAppender commentLikeAppender;

	@Transactional
	public Long createComment(Long postId, String content, Anonymity anonymity, Long parentId){

		// 학생 인증 확인
		userValidator.validateStudentVerification();

		// 부모 댓글 유효성 체크
		commentValidator.validateParent(postId, parentId);

		// 익명 번호 할당
		Optional<Long> anonymousNumber = anonymousNumberAllocator.allocate(postId, anonymity);

		// 댓글 추가
		Long commentId = commentAppender.append(postId, content, anonymity, anonymousNumber, parentId);

		return commentId;
	}

	@Transactional
	public Long deleteComment(Long commentId){

		return commentDeleter.delete(commentId);
	}

	@Transactional
	public Long likeComment(Long commentId){

		// 학생 인증 확인
		userValidator.validateStudentVerification();

		// 댓글 유효성 체크
		commentValidator.validateCommentStatus(commentId);

		// 좋아요 추가
		return commentLikeAppender.append(commentId);
	}
}
