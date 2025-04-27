package com.cotato.kampus.domain.post.implement.port;

import java.util.List;

import com.cotato.kampus.domain.post.domain.PostCategory;

public interface PostCategoryRepository {

	List<PostCategory> findByPostId(Long postId);

	List<Long> findPostIdsByCategoryId(Long categoryId);

	void deleteAll(List<PostCategory> postCategories);

	List<PostCategory> saveAll(List<PostCategory> categories);
}
