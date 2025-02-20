package com.cotato.kampus.domain.comment.application;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dto.CommentDetail;
import com.cotato.kampus.domain.comment.dto.CommentDto;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.application.PostFinder;
import com.cotato.kampus.domain.post.application.PostUpdater;
import com.cotato.kampus.domain.post.dto.PostPreview;
import com.cotato.kampus.domain.user.application.UserValidator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentService {

	private final UserValidator userValidator;
	private final CommentAppender commentAppender;
	private final CommentUpdater commentUpdater;
	private final CommentValidator commentValidator;
	private final CommentDeleter commentDeleter;
	private final AnonymousNumberAllocator anonymousNumberAllocator;
	private final CommentLikeAppender commentLikeAppender;
	private final CommentLikeDeleter commentLikeDeleter;
	private final CommentFinder commentFinder;
	private final CommentMapper commentMapper;
	private final ApiUserResolver apiUserResolver;
	private final PostFinder postFinder;
	private final PostUpdater postUpdater;

	@Transactional
	public Long createComment(Long postId, String content, Long parentId) {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 학생 인증 확인
		userValidator.validateStudentVerification(userId);

		// 부모 댓글 유효성 체크
		commentValidator.validateParent(postId, parentId);

		// 익명 번호 할당
		Long anonymousNumber = anonymousNumberAllocator.allocateAnonymousNumber(postId);

		// 댓글 추가
		Long commentId = commentAppender.append(postId, content, anonymousNumber, parentId);

		// 게시글의 댓글 수 + 1
		postUpdater.increaseComments(postId);

		return commentId;
	}

	@Transactional
	public void deleteComment(Long commentId) {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 댓글 조회
		CommentDto commentDto = commentFinder.findCommentDto(commentId);

		// 작성자 검증
		commentValidator.validateCommentAuthor(userId, commentDto);

		// 댓글 삭제
		commentDeleter.delete(commentId);

		// 게시글의 댓글 수 - 1
		postUpdater.decreaseComments(commentDto.postId());

		// 댓글 좋아요 데이터 삭제
		commentLikeDeleter.delete(commentId);
	}

	@Transactional
	public void likeComment(Long commentId) {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 학생 인증 확인
		userValidator.validateStudentVerification(userId);

		// 댓글 유효성 체크
		commentValidator.validateCommentStatus(commentId);

		// 좋아요 추가
		commentLikeAppender.append(userId, commentId);

		// 댓글 좋아요 수 증가
		commentUpdater.increaseCommentLikes(commentId);
	}

	@Transactional
	public void unlikeComment(Long commentId) {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 좋아요 삭제
		commentLikeAppender.delete(userId, commentId);

		// 댓글 좋아요 수 감소
		commentUpdater.decreaseCommentLikes(commentId);
	}

	@Transactional
	public List<CommentDetail> findAllCommentsForPost(Long postId) {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 이 게시글에 달린 모든 댓글과 대댓글 가져오기
		List<CommentDto> commentDtos = commentFinder.findComments(postId);

		// 댓글 리스트 생성 + 유저 좋아요 여부 매핑
		List<CommentDetail> comments = commentMapper.buildCommentHierarchy(commentDtos, userId);

		return comments;
	}

	@Transactional
	public Slice<PostPreview> getCommentedPosts(int page) {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// // 유저가 댓글 단 게시글 ID를 최신순으로 가져오기
		// List<Long> postIds = commentFinder.findRecentPostIdsByUserId(userId);
		//
		// // 최신 댓글 기준으로 정렬된 게시글 가져오기
		// Slice<PostWithPhotos> posts = postFinder.findUserCommentedPosts(postIds, page);

		return postFinder.getCommentedPosts(userId, page);
	}
}