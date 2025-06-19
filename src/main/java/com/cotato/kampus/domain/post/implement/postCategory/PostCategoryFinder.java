package com.cotato.kampus.domain.post.implement.postCategory;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.domain.PostCategory;
import com.cotato.kampus.domain.post.implement.port.PostCategoryRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCategoryFinder {

	private final PostCategoryRepository postCategoryRepository;

	public List<Long> findAllPostIdsByCategoryId(Long categoryId) {
		return postCategoryRepository.findPostIdsByCategoryId(categoryId);
	}

	public List<Long> findCategoryIdsByPostId(Long postId) {
		return postCategoryRepository.findByPostId(postId).stream()
			.map(PostCategory::getCategoryId)
			.toList();
	}
}
