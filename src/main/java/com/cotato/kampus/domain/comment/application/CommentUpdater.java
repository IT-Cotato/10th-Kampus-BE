package com.cotato.kampus.domain.comment.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.domain.Comment;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentUpdater {

	private final CommentFinder commentFinder;

	@Transactional
	public void increaseCommentLikes(Long commentId) {
		Comment comment = commentFinder.findComment(commentId);

		comment.increaseLikes();
	}

	@Transactional
	public void decreaseCommentLikes(Long commentId){
		Comment comment = commentFinder.findComment(commentId);

		comment.decreaseLikes();
	}
}
