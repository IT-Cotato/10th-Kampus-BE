package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cotato.kampus.domain.post.dao.entity.TrendingPostEntity;

public interface TrendingPostJpaRepository extends JpaRepository<TrendingPostEntity, Long> {

	boolean existsByPostId(Long postId);

	void deleteByPostId(Long postId);

	@Query("SELECT t.postId FROM TrendingPostEntity t")
	List<Long> findAllPostIds();
}
