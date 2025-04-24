package com.cotato.kampus.domain.post.implement.port;

import java.util.List;

import com.cotato.kampus.domain.post.domain.TrendingPost;

public interface TrendingPostRepository {

	TrendingPost save(TrendingPost trendingPost);

	boolean existsByPostId(Long postId);

	void deleteByPostId(Long postId);

	List<Long> findAllPostIds();
}
