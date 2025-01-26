package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostScrapRepository;
import com.cotato.kampus.domain.post.domain.PostScrap;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapUpdater {

	private final PostScrapRepository postScrapRepository;

	public void append(Long postId, Long userId) {
		PostScrap postScrap = PostScrap.builder()
			.postId(postId)
			.userId(userId)
			.build();

		postScrapRepository.save(postScrap);
	}

	public void delete(Long postId, Long userId) {
		PostScrap postScrap = postScrapRepository.findByUserIdAndPostId(userId, postId)
			.orElseThrow(() -> new AppException(ErrorCode.POST_SCRAP_NOT_EXIST));

		postScrapRepository.delete(postScrap);
	}
}
