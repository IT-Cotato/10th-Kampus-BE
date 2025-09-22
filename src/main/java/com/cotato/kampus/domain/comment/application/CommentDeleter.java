package com.cotato.kampus.domain.comment.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dao.CommentRepository;
import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.comment.dto.CommentDto;
import com.cotato.kampus.domain.comment.enums.CommentStatus;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.post.implement.post.PostUpdater;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDeleter {

	private final CommentRepository commentRepository;
	private final CommentFinder commentFinder;
	private final CommentLikeDeleter commentLikeDeleter;
	private final PostFinder postFinder;
	private final PostUpdater postUpdater;

	@Transactional
	public void delete(CommentDto commentDto) {
		Comment comment = commentFinder.findComment(commentDto.commentId());
		boolean hasReplies = commentRepository.existsByParentId(commentDto.commentId());

		// 댓글 좋아요 삭제 처리한다
		commentLikeDeleter.deleteAllByCommentId(commentDto.commentId());

		if (hasReplies) {
			// 대댓글이 있는 부모 댓글: 마스킹 처리, 댓글 수 유지
			comment.setCommentStatus(CommentStatus.MASKED);
		} else {
			// 대댓글이 없는 경우: REMOVED 상태로 변경, 댓글 수 감소
			Post post = postFinder.find(commentDto.postId());
			postUpdater.decreaseCommentCount(post);
			comment.setCommentStatus(CommentStatus.REMOVED);
		}
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
