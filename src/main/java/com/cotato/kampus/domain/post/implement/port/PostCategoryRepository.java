package com.cotato.kampus.domain.post.implement.port;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.domain.PostCategory;

public interface PostCategoryRepository {

	List<PostCategory> findByPostId(Long postId);

	Slice<Long> findPostIdsByCategoryId(Long categoryId, Pageable pageable);

	void deleteAll(List<PostCategory> postCategories);

	List<PostCategory> saveAll(List<PostCategory> categories);
}
