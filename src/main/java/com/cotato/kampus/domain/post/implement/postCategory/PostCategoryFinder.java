package com.cotato.kampus.domain.post.implement.postCategory;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.PostDraftCategoryRepository;
import com.cotato.kampus.domain.post.domain.PostDraftCategory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCategoryFinder {
	private final PostDraftCategoryRepository postDraftCategoryRepository;

	public List<Long> findAllCategoryId(Long postDraftId) {
		List<PostDraftCategory> postDraftCategories = postDraftCategoryRepository.findByPostDraftId(postDraftId);

		return postDraftCategories.stream()
			.map(PostDraftCategory::getCategoryId)
			.toList();
	}
}
