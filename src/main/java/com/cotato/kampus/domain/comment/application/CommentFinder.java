package com.cotato.kampus.domain.comment.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dao.CommentRepository;
import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.comment.dto.CommentDto;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentFinder {

	private final CommentRepository commentRepository;

	public Comment findComment(Long commentId){
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
	}

	public List<CommentDto> findComments(Long postId){
		List<Comment> comments = commentRepository.findAllByPostId(postId);
		List<CommentDto> commentDtos = comments.stream()
			.map(CommentDto::from)
			.toList();

		return commentDtos;
	}

	public List<CommentDto> findChildComments(Long parentId){
		List<Comment> comments = commentRepository.findAllByParentId(parentId);
		List<CommentDto> commentDtos = comments.stream()
			.map(CommentDto::from)
			.toList();

		return commentDtos;
	}
}
