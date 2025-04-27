package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.dao.entity.TrendingPostEntity;
import com.cotato.kampus.domain.post.domain.TrendingPost;
import com.cotato.kampus.domain.post.implement.port.TrendingPostRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TrendingPostRepositoryImpl implements TrendingPostRepository {

	private final TrendingPostJpaRepository trendingPostJpaRepository;

	@Override
	public TrendingPost save(TrendingPost trendingPost) {
		TrendingPostEntity entity = TrendingPostEntity.fromDomain(trendingPost);
		return trendingPostJpaRepository.save(entity).toDomain();
	}

	@Override
	public boolean existsByPostId(Long postId) {
		return trendingPostJpaRepository.existsByPostId(postId);
	}

	@Override
	public void deleteByPostId(Long postId) {
		trendingPostJpaRepository.deleteByPostId(postId);
	}

	@Override
	public List<Long> findAllPostIds() {
		return trendingPostJpaRepository.findAllPostIds();
	}
}
