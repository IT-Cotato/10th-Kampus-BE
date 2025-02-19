package com.cotato.kampus.domain.comment.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dao.CommentLikeRepository;
import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.comment.domain.CommentLike;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikeAppender {

	private final CommentLikeRepository commentLikeRepository;
	private final CommentFinder commentFinder;

	@Transactional
	public void append(Long userId, Long commentId){

		boolean alreadyLiked = commentLikeRepository.existsByUserIdAndCommentId(userId, commentId);

		// 이미 좋아요한 경우 예외처리
		if(alreadyLiked){
			throw new AppException(ErrorCode.ALREADY_LIKED);
		}

		// 좋아요 추가
		CommentLike commentLike = CommentLike.builder()
				.commentId(commentId)
				.userId(userId)
				.build();

		commentLikeRepository.save(commentLike);
	}

	@Transactional
	public void delete(Long userId, Long commentId){
		CommentLike commentLike = commentFinder.findCommentLike(userId, commentId);
		commentLikeRepository.delete(commentLike);

	}
}
