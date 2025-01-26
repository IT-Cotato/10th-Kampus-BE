package com.cotato.kampus.domain.comment.application;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dao.CommentRepository;
import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.comment.enums.CommentStatus;
import com.cotato.kampus.domain.comment.enums.ReportStatus;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.enums.Anonymity;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentAppender {

	private final CommentRepository commentRepository;
	private final ApiUserResolver apiUserResolver;

	@Transactional
	public Long append(Long postId, String content, Anonymity anonymity, Optional<Long> anonymousNumber, Long parentId){

		Long userId = apiUserResolver.getUserId();

		Long anonymousNumberValue = anonymousNumber.orElse(null);

		Comment comment = Comment.builder()
			.userId(userId)
			.postId(postId)
			.content(content)
			.likes(0L)
			.reportStatus(ReportStatus.NORMAL)
			.commentStatus(CommentStatus.NORMAL)
			.anonymity(anonymity)
			.reports(0L)
			.anonymousNumber(anonymousNumberValue)
			.parentId(parentId)
			.build();

		return commentRepository.save(comment).getId();
	}
}
