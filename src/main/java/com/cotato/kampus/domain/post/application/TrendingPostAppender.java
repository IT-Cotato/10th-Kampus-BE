package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.TrendingPostRepository;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.TrendingPost;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class TrendingPostAppender {

	private final TrendingPostRepository trendingPostRepository;
	private final PostFinder postFinder;

	@Transactional
	public void appendTrendingPost(Long postId) {
		Post post = postFinder.getPost(postId);

		if (post.getLikes() >= 3) {
			TrendingPost trendingPost = TrendingPost.builder()
				.postId(postId)
				.boardId(post.getBoardId())
				.build();

			trendingPostRepository.save(trendingPost);
		}
	}
}
