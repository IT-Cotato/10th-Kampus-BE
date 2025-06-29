package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.dao.entity.TemporaryPostCategoryEntity;

public interface TemporaryPostCategoryJpaRepository extends JpaRepository<TemporaryPostCategoryEntity, Long> {

	void deleteAllByTemporaryPostIdIn(List<Long> tempPostIds);

	void deleteAllByTemporaryPostId(Long tempPostId);

	List<TemporaryPostCategoryEntity> findAllByTemporaryPostId(Long tempPostId);
}
