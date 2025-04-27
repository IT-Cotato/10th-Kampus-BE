package com.cotato.kampus.domain.post.implement.postCategory;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.PostCategoryRepository;
import com.cotato.kampus.domain.post.domain.PostCategory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCategoryAppender {
	private final PostCategoryRepository postCategoryRepository;

	public void appendAll(Long postId, List<Long> categoryIds) {
		List<PostCategory> postCategories = categoryIds.stream()
				.map(categoryId -> PostCategory.builder()
					.postId(postId)
					.categoryId(categoryId)
					.build()
				).toList();

		postCategoryRepository.saveAll(postCategories);
	}
}
