package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostScrapRepository;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostValidator {

	private final PostFinder postFinder;
	private final PostScrapRepository postScrapRepository;

	public void validatePostOwner(Long postId, Long userId){
		// Post 조회
		Post post = postFinder.getPost(postId);

		if(!post.getUserId().equals(userId)){
			throw new AppException(ErrorCode.POST_NOT_AUTHOR);
		}
	}

	// 스크랩, 좋아요
	public void validatePostScrap(Long postId, Long userId){
		// Post 조회
		Post post = postFinder.getPost(postId);

		// 작성자가 아닌지 검증
		validateNotPostOwner(post, userId);

		// 중복 스크랩 예외처리
		if(postScrapRepository.existsByUserIdAndPostId(userId, postId)){
			throw new AppException(ErrorCode.POST_SCRAP_DUPLICATED);
		}
	}


	public void validateNotPostOwner(Post post, Long userId){

		if(post.getUserId().equals(userId)){
			throw new AppException(ErrorCode.POST_SCRAP_FORBIDDEN);
		}
	}


}
