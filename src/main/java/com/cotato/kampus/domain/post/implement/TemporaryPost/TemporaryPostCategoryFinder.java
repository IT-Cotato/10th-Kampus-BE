package com.cotato.kampus.domain.post.implement.TemporaryPost;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.domain.TemporaryPostCategory;
import com.cotato.kampus.domain.post.implement.port.TemporaryPostCategoryRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TemporaryPostCategoryFinder {

	private final TemporaryPostCategoryRepository temporaryPostCategoryRepository;

	public List<Long> findCategoryIdsByTempPostId(Long tempPostId) {
		return temporaryPostCategoryRepository.findAllByTemporaryPostId(tempPostId).stream()
			.map(TemporaryPostCategory::getCategoryId)
			.toList();
	}
}
