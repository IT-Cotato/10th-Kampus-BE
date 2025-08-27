package com.cotato.kampus.domain.post.implement.postScrap;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.PostScrapRepository;
import com.cotato.kampus.domain.post.domain.PostScrap;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostScrapFinder {

	private final PostScrapRepository postScrapRepository;

	public boolean isPostScrappedByUser(Long postId, Long userId) {
		return postScrapRepository.existsByPostIdAndUserId(postId, userId);
	}

	public PostScrap find(Long userId, Long postId) {
		PostScrap postScrap = postScrapRepository.findByPostIdAndUserId(postId, userId)
			.orElseThrow(() -> new AppException(ErrorCode.POST_SCRAP_NOT_EXIST));

		return postScrap;
	}

	public List<Long> findAllByUserId(Long userId) {
		return postScrapRepository.findAllPostIdsByUserId(userId);
	}
}
