package com.cotato.kampus.domain.category.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.category.dao.entity.CategoryEntity;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {

	Optional<CategoryEntity> findByCategoryName(String categoryName);

	boolean existsByCategoryName(String categoryName);
}
