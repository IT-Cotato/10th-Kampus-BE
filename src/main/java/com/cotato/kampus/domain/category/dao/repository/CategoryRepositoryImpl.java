package com.cotato.kampus.domain.category.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.category.dao.entity.CategoryEntity;
import com.cotato.kampus.domain.category.domain.Category;
import com.cotato.kampus.domain.category.implement.port.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

	private final CategoryJpaRepository categoryJpaRepository;

	@Override
	public Category save(Category category) {
		CategoryEntity entity = CategoryEntity.fromDomain(category);
		return categoryJpaRepository.save(entity).toDomain();
	}

	@Override
	public Optional<Category> findById(Long id) {
		return categoryJpaRepository.findById(id)
			.map(CategoryEntity::toDomain);
	}

	@Override
	public Optional<Category> findByCategoryName(String categoryName) {
		return categoryJpaRepository.findByCategoryName(categoryName)
			.map(CategoryEntity::toDomain);
	}

	@Override
	public List<Category> findAll() {
		return categoryJpaRepository.findAll().stream()
			.map(CategoryEntity::toDomain)
			.toList();
	}

	@Override
	public boolean existsByCategoryName(String categoryName) {
		return categoryJpaRepository.existsByCategoryName(categoryName);
	}

	@Override
	public List<Category> findAllByIdIn(List<Long> categoryIds) {
		return categoryJpaRepository.findAllByIdIn(categoryIds).stream()
			.map(CategoryEntity::toDomain)
			.toList();
	}
}
