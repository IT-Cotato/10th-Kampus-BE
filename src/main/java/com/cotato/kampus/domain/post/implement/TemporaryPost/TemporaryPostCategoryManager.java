package com.cotato.kampus.domain.post.implement.TemporaryPost;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.domain.TemporaryPostCategory;
import com.cotato.kampus.domain.post.implement.port.TemporaryPostCategoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class TemporaryPostCategoryManager {

	private final TemporaryPostCategoryRepository temporaryPostCategoryRepository;

	public void appendAll(Long tempPostId, List<Long> categoryIds) {
		List<TemporaryPostCategory> tempPostCategories = categoryIds.stream()
			.map(categoryId -> TemporaryPostCategory.builder()
				.temporaryPostId(tempPostId)
				.categoryId(categoryId)
				.build())
			.toList();

		temporaryPostCategoryRepository.saveAll(tempPostCategories);
	}

	public void deleteAllByTemporaryPostIds(List<Long> tempPostIds){
		temporaryPostCategoryRepository.deleteAllByTemporaryPostIdIn(tempPostIds);
	}

	public void deleteAllByTemporaryPostId(Long tempPostId){
		temporaryPostCategoryRepository.deleteAllByTemporaryPostId(tempPostId);
	}
}
