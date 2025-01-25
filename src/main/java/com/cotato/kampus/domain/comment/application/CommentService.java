package com.cotato.kampus.domain.comment.application;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dto.CommentDto;
import com.cotato.kampus.domain.comment.dto.CommentDetail;
import com.cotato.kampus.domain.comment.dto.CommentSummary;
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
	private final AuthorResolver authorResolver;
	private final CommentLikeAppender commentLikeAppender;
	private final CommentFinder commentFinder;
	private final CommentMapper commentMapper;

	@Transactional
	public Long createComment(Long postId, String content, Anonymity anonymity, Long parentId){

		// 학생 인증 확인
		userValidator.validateStudentVerification();

		// 부모 댓글 유효성 체크
		commentValidator.validateParent(postId, parentId);

		// 익명 번호 할당
		Optional<Long> anonymousNumber = authorResolver.allocateAnonymousNumber(postId, anonymity);

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

	@Transactional
	public List<CommentDetail> findAllCommentsForPost(Long postId){

		// 이 게시글에 달린 모든 댓글과 대댓글 가져오기
		List<CommentDto> commentDtos = commentFinder.findComments(postId);

		// 댓글 리스트 생성
		List<CommentDetail> comments = commentMapper.buildCommentHierarchy(commentDtos);

		return comments;
	}

	@Transactional
	public Slice<CommentSummary> findUserComments(int page){

		return commentFinder.findUserComments(page);
	}
}