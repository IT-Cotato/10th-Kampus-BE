package com.cotato.kampus.domain.post.implement.trendingPost;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.TrendingPostRepository;
import com.cotato.kampus.domain.post.domain.TrendingPost;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class TrendingPostManager {

	private static final int TRENDING_LIKE_THRESHOLD = 5;

	private final TrendingPostFinder trendingPostFinder;
	private final TrendingPostRepository trendingPostRepository;

	public void handleLikeCountChange(Long postId, int newLikeCount) {
		boolean shouldBeTrending = newLikeCount >= TRENDING_LIKE_THRESHOLD;
		boolean isTrending = trendingPostFinder.existsByPostId(postId);

		if (shouldBeTrending && !isTrending) {
			append(postId);
		} else if (!shouldBeTrending && isTrending) {
			deleteByPostId(postId);
		}
	}

	public TrendingPost append(Long postId) {
		TrendingPost trendingPost = TrendingPost.builder()
			.postId(postId)
			.build();
		return trendingPostRepository.save(trendingPost);
	}

	public void deleteByPostId(Long postId) {
		trendingPostRepository.deleteByPostId(postId);
	}
}