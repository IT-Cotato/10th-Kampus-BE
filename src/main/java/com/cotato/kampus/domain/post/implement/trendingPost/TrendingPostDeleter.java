package com.cotato.kampus.domain.post.implement.trendingPost;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.TrendingPostRepository;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class TrendingPostDeleter {

	private final TrendingPostRepository trendingPostRepository;

	public void deleteByPostId(Long postId) {
		trendingPostRepository.deleteByPostId(postId);
	}
}
