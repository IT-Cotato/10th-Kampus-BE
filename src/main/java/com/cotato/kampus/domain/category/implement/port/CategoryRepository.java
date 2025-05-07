package com.cotato.kampus.domain.category.implement.port;

import java.util.List;
import java.util.Optional;

import com.cotato.kampus.domain.category.domain.Category;

public interface CategoryRepository {

	Category save(Category category);

	Optional<Category> findById(Long id);

	Optional<Category> findByCategoryName(String categoryName);

	List<Category> findAll();

	boolean existsByCategoryName(String categoryName);
}
