package com.cotato.kampus.domain.post.implement.post;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.PostRepository;
import com.cotato.kampus.domain.post.implement.port.TrendingPostRepository;
import com.cotato.kampus.domain.post.domain.Post;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class PostDeleter {
	private final PostRepository postRepository;
	private final PostFinder postFinder;
	private final TrendingPostRepository trendingPostRepository;
	private static final int TRENDING_LIKE_THRESHOLD = 3;

	public void delete(Post post) {
		postRepository.delete(post);
	}

	public void deletePostsByBoardIds(List<Long> boardIds) {
		postRepository.deleteAllByBoardIdIn(boardIds);
	}

	public void deleteTrendingPost(Long postId) {
		Post post = postFinder.getPost(postId);

		if (post.getLikes() == TRENDING_LIKE_THRESHOLD) {
			trendingPostRepository.deleteByPostId(postId);
		}
	}
}
