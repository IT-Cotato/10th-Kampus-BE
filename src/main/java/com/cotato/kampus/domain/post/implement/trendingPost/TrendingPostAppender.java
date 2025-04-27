package com.cotato.kampus.domain.post.implement.trendingPost;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.TrendingPostRepository;
import com.cotato.kampus.domain.post.domain.TrendingPost;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class TrendingPostAppender {

	private final TrendingPostRepository trendingPostRepository;

	public TrendingPost append(Long postId) {
		TrendingPost trendingPost = TrendingPost.builder()
			.postId(postId)
			.build();
		return trendingPostRepository.save(trendingPost);
	}
}
