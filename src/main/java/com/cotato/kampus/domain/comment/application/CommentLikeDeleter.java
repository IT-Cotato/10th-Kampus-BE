package com.cotato.kampus.domain.comment.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dao.CommentLikeRepository;
import com.cotato.kampus.domain.comment.domain.CommentLike;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikeDeleter {

	private final CommentLikeRepository commentLikeRepository;

	@Transactional
	public void delete(Long commentId) {
		List<CommentLike> commentLikes = commentLikeRepository.findAllByCommentId(commentId);

		commentLikeRepository.deleteAll(commentLikes);
	}
}
