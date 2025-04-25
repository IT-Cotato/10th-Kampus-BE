package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.dao.entity.PostCategoryEntity;
import com.cotato.kampus.domain.post.domain.PostCategory;
import com.cotato.kampus.domain.post.implement.port.PostCategoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostCategoryRepositoryImpl implements PostCategoryRepository {

	private final PostCategoryJpaRepository postCategoryJpaRepository;

	@Override
	public List<PostCategory> findByPostId(Long postId) {
		return postCategoryJpaRepository.findByPostId(postId).stream()
			.map(PostCategoryEntity::toDomain)
			.toList();
	}

	@Override
	public List<Long> findPostIdsByCategoryId(Long categoryId) {
		return postCategoryJpaRepository.findPostIdsByCategoryId(categoryId);
	}

	@Override
	public void deleteAll(List<PostCategory> postCategories) {
		List<PostCategoryEntity> postCategoryEntities = postCategories.stream()
				.map(PostCategoryEntity::fromDomain)
				.toList();

		postCategoryJpaRepository.deleteAll(postCategoryEntities);
	}

	@Override
	public List<PostCategory> saveAll(List<PostCategory> postCategories) {
		List<PostCategoryEntity> postCategoryEntities = postCategories.stream()
			.map(PostCategoryEntity::fromDomain)
			.toList();

		return postCategoryJpaRepository.saveAll(postCategoryEntities).stream()
			.map(PostCategoryEntity::toDomain)
			.toList();
	}
}
