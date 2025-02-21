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
import com.cotato.kampus.domain.post.dto.PostDto;
import com.cotato.kampus.domain.user.application.UserFinder;
import com.cotato.kampus.domain.user.dto.UserDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AnonymousNumberAllocator {

	private final CommentRepository commentRepository;
	private final ApiUserResolver apiUserResolver;
	private final PostUpdater postUpdater;
	private final UserFinder userFinder;

	public Long allocateAnonymousNumber(PostDto postDto, UserDto userDto){
		// 작성자가 아닌 경우에만 익명 번호 증가
		if(!postDto.userId().equals(userDto.id())) {
			// 해당 Post에 현재 User의 댓글 작성 여부 확인
			Optional<Comment> comment = commentRepository.findFirstByPostIdAndUserId(
				postDto.id(), userDto.id()
			);
			return comment.map(Comment::getAnonymousNumber)
				.orElseGet(() -> (postUpdater.increaseNextAnonymousNumber(postDto.id())));
		} else {
			return null;
		}
	}

	public String resolveAuthorName(CommentDto commentDto){

		if(commentDto.anonymousNumber() != null){
			return "Anonymous" + commentDto.anonymousNumber();
		}

		return "Author";
	}
}
