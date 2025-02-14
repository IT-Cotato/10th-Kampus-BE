package com.cotato.kampus.domain.comment.application;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dao.CommentRepository;
import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.comment.dto.CommentDto;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.application.PostUpdater;
import com.cotato.kampus.domain.user.application.UserFinder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorResolver {

	private final CommentRepository commentRepository;
	private final ApiUserResolver apiUserResolver;
	private final PostUpdater postUpdater;
	private final UserFinder userFinder;

	public Long allocateAnonymousNumber(Long postId){
		// 해당 Post에 현재 User의 댓글 작성 여부 확인
		Optional<Comment> comment = commentRepository.findFirstByPostIdAndUserId(
			postId, apiUserResolver.getUserId()
		);

		return comment.map(Comment::getAnonymousNumber)
			.orElseGet(() ->(postUpdater.increaseNextAnonymousNumber(postId)));
	}

	public String resolveAuthorName(CommentDto commentDto){

		if(commentDto.anonymity() == Anonymity.ANONYMOUS){
			return "Anonymous" + commentDto.anonymousNumber();
		}

		String nickname = userFinder.findById(commentDto.userId()).getNickname();

		return nickname;
	}
}
