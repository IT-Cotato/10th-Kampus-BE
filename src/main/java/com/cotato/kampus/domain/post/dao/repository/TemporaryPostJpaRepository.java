package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.dao.entity.TemporaryPostEntity;

public interface TemporaryPostJpaRepository extends JpaRepository<TemporaryPostEntity, Long> {

	List<TemporaryPostEntity> findAllByIdIn(List<Long> ids);

	void deleteAllByUserId(Long userId);
}
