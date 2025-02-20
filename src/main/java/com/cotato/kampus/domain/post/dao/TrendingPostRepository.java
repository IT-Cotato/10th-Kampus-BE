package com.cotato.kampus.domain.post.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.domain.TrendingPost;

@Repository
public interface TrendingPostRepository extends JpaRepository<TrendingPost, Long> {

	void deleteByPostId(Long postId);

	@Query("SELECT t.postId FROM TrendingPost t")
	List<Long> findAllPostIds();
}
