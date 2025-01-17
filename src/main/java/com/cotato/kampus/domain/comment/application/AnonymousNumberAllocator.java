package com.cotato.kampus.domain.comment.application;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dao.CommentRepository;
import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.application.PostUpdater;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AnonymousNumberAllocator {

	private final CommentRepository commentRepository;
	private final ApiUserResolver apiUserResolver;
	private final PostUpdater postUpdater;

	public Optional<Long> allocate(Long postId, Anonymity anonymity){

		// 익명인 경우
		if(anonymity == Anonymity.ANONYMOUS){
			// 해당 Post에 현재 User의 댓글 작성 여부 확인
			Optional<Comment> comment = commentRepository.findFirstByPostIdAndUserIdAndAnonymity(
				postId, apiUserResolver.getUserId(), anonymity
			);

			return comment.map(Comment::getAnonymousNumber)
				.or(() -> Optional.ofNullable(postUpdater.increaseNextAnonymousNumber(postId)));
		} else {
			return Optional.empty();
		}
	}
}
