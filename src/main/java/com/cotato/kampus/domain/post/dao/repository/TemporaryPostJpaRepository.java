package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.dao.entity.TemporaryPostEntity;

public interface TemporaryPostJpaRepository extends JpaRepository<TemporaryPostEntity, Long> {

	List<TemporaryPostEntity> findAllByIdIn(List<Long> ids);

	List<TemporaryPostEntity> findAllByUserId(Long userId);

	Slice<TemporaryPostEntity> findAllByUserId(Long userId, Pageable pageable);

	void deleteAllByIdIn(List<Long> tempPostIds);

	void deleteAllByUserId(Long userId);
}
