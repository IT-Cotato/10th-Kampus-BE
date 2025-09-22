package com.cotato.kampus.domain.comment.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dao.CommentRepository;
import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.comment.dto.CommentDto;
import com.cotato.kampus.domain.comment.enums.CommentStatus;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentFinder {
	private final CommentRepository commentRepository;

	public Comment findComment(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
	}

	public CommentDto findCommentDto(Long commentId) {
		return CommentDto.from(findComment(commentId));
	}

	public List<CommentDto> findAllDtoByPostId(Long postId) {
		List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedTimeAsc(postId);
		List<CommentDto> commentDtos = comments.stream()
			.filter(comment -> !isRemovedStatus(comment.getCommentStatus()))
			.map(CommentDto::from)
			.toList();

		return commentDtos;
	}

	private boolean isRemovedStatus(CommentStatus status) {
		return status == CommentStatus.REMOVED
			|| status == CommentStatus.REMOVED_BY_ADMIN;
	}

	public List<Comment> findAllByPostId(Long postId) {
		return commentRepository.findAllByPostIdOrderByCreatedTimeAsc(postId);
	}
}
