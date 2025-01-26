package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostScrapRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapValidator {

	private final PostScrapRepository postScrapRepository;
	private final PostAuthorResolver postAuthorResolver;

	public void validatePostScrap(Long postId, Long userId){
		Long authorId = postAuthorResolver.getAuthorId(postId);

		// 본인 게시글 또는 이미 스크랩한 게시글은 예외처리
		if(userId.equals(authorId)){
			throw new AppException(ErrorCode.POST_SCRAP_FORBIDDEN);
		}

		if(postScrapRepository.existsByUserIdAndPostId(userId, postId)){
			throw new AppException(ErrorCode.POST_SCRAP_DUPLICATED);
		}
	}
}
