package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostScrapRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostScrapFinder {

	private final PostScrapRepository postScrapRepository;

	public boolean isPostScrappedByUser(Long userId, Long postId) {
		return postScrapRepository.existsByUserIdAndPostId(userId, postId);
	}
}
