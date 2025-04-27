package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.dao.entity.PostCategoryEntity;

public interface PostCategoryJpaRepository extends JpaRepository<PostCategoryEntity, Long> {

	List<PostCategoryEntity> findByPostId(Long postId);

	List<Long> findPostIdsByCategoryId(Long categoryId);
}
