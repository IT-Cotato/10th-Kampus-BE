package com.cotato.kampus.domain.post.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.domain.PostCategory;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {

	List<PostCategory> findByPostId(Long postId);
}
