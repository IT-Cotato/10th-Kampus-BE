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
		// 대댓글이 없는 댓글이 삭제될 때만 게시글의 전체 댓글 수를 감소시킨다
		// 대댓글이 있다면 "삭제된 댓글입니다"로 내용만 마스킹 처리되므로 전체 댓글 수는 유지된다
		boolean hasReplies = commentRepository.existsByParentId(commentDto.commentId());
		if(!hasReplies) {
			Post post = postFinder.find(commentDto.postId());
			postUpdater.decreaseCommentCount(post);
		}

		// 댓글을 논리적으로 삭제 처리한다
		Comment comment = commentFinder.findComment(commentDto.commentId());
		comment.setCommentStatus(CommentStatus.DELETED_BY_USER);

		// 댓글 좋아요 삭제 처리한다
		commentLikeDeleter.deleteAllByCommentId(commentDto.commentId());
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
