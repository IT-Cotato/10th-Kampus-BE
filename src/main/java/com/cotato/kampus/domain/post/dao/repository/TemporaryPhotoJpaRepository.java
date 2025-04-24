package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.dao.entity.TemporaryPhotoEntity;

public interface TemporaryPhotoJpaRepository extends JpaRepository<TemporaryPhotoEntity, Long> {

	Optional<TemporaryPhotoEntity> findFirstByTemporaryPostIdOrderByCreatedTimeAsc(Long tempPostId);

	List<TemporaryPhotoEntity> findAllByTemporaryPostId(Long tempPostId);

	List<TemporaryPhotoEntity> findAllByTemporaryPostIdIn(List<Long> tempPostIds);

	void deleteAllByTemporaryPostId(Long tempPostId);

	void deleteAllByTemporaryPostIdIn(List<Long> tempPostIds);
}
