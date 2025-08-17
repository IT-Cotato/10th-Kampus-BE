package com.cotato.kampus.domain.comment.application;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.comment.dto.CommentDetail;
import com.cotato.kampus.domain.comment.dto.CommentDto;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostThumbnailWithBoardName;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.post.implement.post.PostUpdater;
import com.cotato.kampus.domain.post.implement.postImage.PostPhotoFinder;
import com.cotato.kampus.domain.post.implement.postSrcap.PostScrapFinder;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.dto.UserDto;

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
	private final PostPhotoFinder postPhotoFinder;
	private final BoardFinder boardFinder;
	private final PostScrapFinder postScrapFinder;

	/**
	 18    * 게시글에 새로운 댓글을 생성하고 관련 카운터를 업데이트
	 19    *
	 20    *@param postId   댓글을 작성할 게시글 ID
	 21    *@param content  댓글 내용
	 22    *@param parentId 부모 댓글 ID (대댓글이 아닐 경우 null)
	 23    *@param targetId 답글 대상 댓글 ID (대댓글이 아닐 경우 null)
	 24    *@return 생성된 댓글 ID
	 25    */
	@Transactional
	public Long createComment(Long postId, String content, Long parentId, Long targetId) { // targetId는 답글 대상
		UserDto userDto = apiUserResolver.getCurrentUserDto();
		Post post = postFinder.find(postId);

		userValidator.validateStudentVerification(userDto);
		commentValidator.validateParent(postId, parentId);

		boolean isAuthor = post.getUserId().equals(userDto.id());
		AnonymousAllocationResult allocationResult = anonymousNumberAllocator.allocateAnonymousNumber(post, userDto, isAuthor);

		Long commentId = commentAppender.append(postId, content, allocationResult.anonymousNumber(), parentId, targetId);

		updatePostCountersForNewComment(post, allocationResult);

		return commentId;
	}

	/**
	 * 새 댓글 생성 후 게시글의 카운터를 업데이트
	 * 새로운 익명 댓글 작성 시 익명 카운터도 함께 증가
	 */
	private Post updatePostCountersForNewComment(Post post, AnonymousAllocationResult result) {
		if (result.needsIncrement()) {
			return postUpdater.increaseCommentAndAnonymousCount(post);
		} else {
			return postUpdater.increaseCommentCount(post);
		}
	}

	@Transactional
	public void deleteComment(Long commentId) {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 댓글 조회
		CommentDto commentDto = commentFinder.findCommentDto(commentId);

		// 유효성 검증
		commentValidator.validateCommentAuthor(userId, commentDto);
		commentValidator.validateCommentStatus(commentId);

		// 댓글 상태 업데이트
		commentDeleter.delete(commentId);

		// 게시글의 댓글 수 - 1
		Post post = postFinder.find(commentDto.postId());

		postUpdater.decreaseCommentCount(post);
	}

	@Transactional
	public void likeComment(Long commentId) {
		// 유저 조회
		UserDto userDto = apiUserResolver.getCurrentUserDto();

		// 학생 인증 확인
		userValidator.validateStudentVerification(userDto);

		// 댓글 유효성 체크
		commentValidator.validateCommentStatus(commentId);

		// 좋아요 추가
		commentLikeAppender.append(userDto.id(), commentId);

		// 댓글 좋아요 수 증가
		commentUpdater.increaseCommentLikes(commentId);
	}

	@Transactional
	public void unlikeComment(Long commentId) {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 좋아요 삭제
		commentLikeDeleter.delete(userId, commentId);

		// 댓글 좋아요 수 감소
		commentUpdater.decreaseCommentLikes(commentId);
	}

	@Transactional
	public List<CommentDetail> findAllCommentsForPost(Long postId) {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 이 게시글에 달린 모든 댓글과 대댓글 가져오기
		List<CommentDto> commentDtos = commentFinder.findAllDtoByPostId(postId);

		// 댓글 리스트 생성 + 유저 좋아요 여부 매핑
		List<CommentDetail> comments = commentMapper.buildCommentHierarchy(commentDtos, userId);

		return comments;
	}

	@Transactional
	public Slice<PostThumbnailWithBoardName> getCommentedPosts(int page) {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		Slice<Post> userCommentedPosts = postFinder.findCommentedPosts(userId, page);

		// TODO: Mapper로 묶어
		return userCommentedPosts.map(post -> {
			String thumbnail = postPhotoFinder.findFirstPhoto(post.getId());
			Board board = boardFinder.findBoard(post.getBoardId());
			boolean isScrapped = postScrapFinder.isPostScrappedByUser(userId, post.getId());
			return PostThumbnailWithBoardName.from(post, board, thumbnail, isScrapped);
		});
	}
}