package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostCategoryRepository;
import com.cotato.kampus.domain.post.dao.PostDraftCategoryRepository;
import com.cotato.kampus.domain.post.domain.PostCategory;
import com.cotato.kampus.domain.post.domain.PostDraftCategory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCategoryAppender {
	private final PostCategoryRepository postCategoryRepository;
	private final PostDraftCategoryRepository postDraftCategoryRepository;

	public void appendAll(Long postId, List<Long> categoryIds) {
		List<PostCategory> postCategories = categoryIds.stream()
				.map(categoryId -> PostCategory.builder()
					.postId(postId)
					.categoryId(categoryId)
					.build()
				).toList();

		postCategoryRepository.saveAll(postCategories);
	}

	public void appendAllDraftCategory(Long postDraftId, List<Long> categoryIds) {
		List<PostDraftCategory> postDraftCategories = categoryIds.stream()
			.map(categoryId -> PostDraftCategory.builder()
				.postDraftId(postDraftId)
				.categoryId(categoryId)
				.build()
			).toList();

		postDraftCategoryRepository.saveAll(postDraftCategories);
	}
}
