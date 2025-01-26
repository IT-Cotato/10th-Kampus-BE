package com.cotato.kampus.domain.comment.application;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dao.CommentRepository;
import com.cotato.kampus.domain.comment.domain.Comment;
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
	private final ApiUserResolver apiUserResolver;

	public Comment findComment(Long commentId){
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
	}

	public List<CommentDto> findComments(Long postId){
		List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedTimeAsc(postId);
		List<CommentDto> commentDtos = comments.stream()
			.map(CommentDto::from)
			.toList();

		return commentDtos;
	}

	public Slice<CommentSummary> findUserComments(int page){

		Long userId = apiUserResolver.getUserId();

		PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdTime"));
		Slice<Comment> comments = commentRepository.findAllByUserId(userId, pageRequest);

		return comments.map(CommentSummary::from);
	}
}
