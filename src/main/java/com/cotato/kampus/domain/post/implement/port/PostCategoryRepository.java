package com.cotato.kampus.domain.post.implement.port;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.domain.PostCategory;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {

	List<PostCategory> findByPostId(Long postId);

	@Query("SELECT p.postId FROM PostCategory p WHERE p.categoryId = :categoryId")
	Slice<Long> findPostIdsByCategoryId(Long categoryId, Pageable pageable);
}
