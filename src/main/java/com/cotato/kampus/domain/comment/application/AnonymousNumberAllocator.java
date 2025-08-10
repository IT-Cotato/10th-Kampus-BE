package com.cotato.kampus.domain.comment.application;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dao.CommentRepository;
import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.comment.dto.CommentDto;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.implement.post.PostUpdater;
import com.cotato.kampus.domain.user.dto.UserDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AnonymousNumberAllocator {

	private final CommentRepository commentRepository;

	public AnonymousAllocationResult allocateAnonymousNumber(Post post, UserDto userDto, boolean isAuthor){
		// 작성자가 아닌 경우에만 익명 번호 증가
		if(!isAuthor) {
			// 기존 댓글작성 여부 확인
			Optional<Comment> comment = commentRepository.findFirstByPostIdAndUserId(post.getId(), userDto.id());

			if (comment.isPresent()) {
				// 기존 댓글이 있으면, 번호만 반환. 카운터 증가 필요 없음(false)
				return AnonymousAllocationResult.of(comment.get().getAnonymousNumber(), false);
			} else {
				// 첫 댓글이면, 새 번호를 계산하고 카운터 증가 필요함(true)
				return AnonymousAllocationResult.of(post.getAnonymousCount() + 1, true);
			}
		} else {
			// 글쓴이면, 익명 번호 없고 카운터 증가도 필요 없음(false)
			return AnonymousAllocationResult.of(null, false);
		}
	}

	public String resolveAuthorName(CommentDto commentDto){

		if(commentDto.anonymousNumber() != null){
			return "Anonymous" + commentDto.anonymousNumber();
		}

		return "Author";
	}
}
