package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cotato.kampus.domain.post.dao.entity.PostCategoryEntity;

public interface PostCategoryJpaRepository extends JpaRepository<PostCategoryEntity, Long> {

	List<PostCategoryEntity> findByPostId(Long postId);

	@Query("SELECT p.postId FROM PostCategoryEntity p WHERE p.categoryId = :categoryId")
	Slice<Long> findPostIdsByCategoryId(Long categoryId, Pageable pageable);
}
