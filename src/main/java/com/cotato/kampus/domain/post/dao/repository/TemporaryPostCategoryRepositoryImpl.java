package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.dao.entity.TemporaryPostCategoryEntity;
import com.cotato.kampus.domain.post.domain.TemporaryPostCategory;
import com.cotato.kampus.domain.post.implement.port.TemporaryPostCategoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TemporaryPostCategoryRepositoryImpl implements TemporaryPostCategoryRepository {

	private final TemporaryPostCategoryJpaRepository temporaryPostCategoryJpaRepository;

	@Override
	public void saveAll(List<TemporaryPostCategory> temporaryPostCategories) {
		List<TemporaryPostCategoryEntity> entities = temporaryPostCategories.stream()
			.map(TemporaryPostCategoryEntity::fromDomain)
			.toList();
		temporaryPostCategoryJpaRepository.saveAll(entities);
	}

	@Override
	public void deleteAllByTemporaryPostIdIn(List<Long> tempPostIds) {
		temporaryPostCategoryJpaRepository.deleteAllByTemporaryPostIdIn(tempPostIds);
	}

	@Override
	public void deleteAllByTemporaryPostId(Long tempPostId) {
		temporaryPostCategoryJpaRepository.deleteAllByTemporaryPostId(tempPostId);
	}
}
