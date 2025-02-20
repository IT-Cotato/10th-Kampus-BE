package com.cotato.kampus.domain.comment.application;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dao.CommentLikeRepository;
import com.cotato.kampus.domain.comment.dao.CommentRepository;
import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.comment.domain.CommentLike;
import com.cotato.kampus.domain.comment.dto.CommentDto;
import com.cotato.kampus.domain.comment.dto.CommentSummary;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentFinder {

	private final CommentRepository commentRepository;
	private final CommentLikeRepository commentLikeRepository;

	public Comment findComment(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
	}

	public CommentDto findCommentDto(Long commentId) {
		return CommentDto.from(findComment(commentId));
	}

	public List<CommentDto> findComments(Long postId) {
		List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedTimeAsc(postId);
		List<CommentDto> commentDtos = comments.stream()
			.map(CommentDto::from)
			.toList();

		return commentDtos;
	}

	// Comment 테이블에서 유저가 최근에 댓글 단 순서로 게시글 ID 가져오기
	public List<Long> findRecentPostIdsByUserId(Long userId) {
		return commentRepository.findRecentPostIdsByUserId(userId)
			.stream()
			.toList();
	}

	public CommentLike findCommentLike(Long userId, Long commentId) {
		return commentLikeRepository.findByUserIdAndCommentId(userId, commentId)
			.orElseThrow(() -> new AppException(ErrorCode.COMMENT_UNLIKE_FORBIDDEN));
	}

}
