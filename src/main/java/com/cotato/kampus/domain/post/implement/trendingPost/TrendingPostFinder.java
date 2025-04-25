package com.cotato.kampus.domain.post.implement.trendingPost;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.TrendingPostRepository;

import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrendingPostFinder {

	private final TrendingPostRepository trendingPostRepository;

	public boolean existsByPostId(Long postId) {
		return trendingPostRepository.existsByPostId(postId);
	}

	public List<Long> findAllPostIds() {
		return trendingPostRepository.findAllPostIds();
	}
}
