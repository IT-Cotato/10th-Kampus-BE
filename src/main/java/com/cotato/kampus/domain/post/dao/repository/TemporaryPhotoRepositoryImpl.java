package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.dao.entity.TemporaryPhotoEntity;
import com.cotato.kampus.domain.post.domain.TemporaryPhoto;
import com.cotato.kampus.domain.post.implement.port.TemporaryPhotoRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TemporaryPhotoRepositoryImpl implements TemporaryPhotoRepository {

	private final TemporaryPhotoJpaRepository temporaryPhotoJpaRepository;

	@Override
	public Optional<TemporaryPhoto> findFirstByTemporaryPostIdOrderByCreatedTimeAsc(Long tempPostId) {
		return temporaryPhotoJpaRepository.findFirstByTemporaryPostIdOrderByCreatedTimeAsc(tempPostId)
			.map(TemporaryPhotoEntity::toDomain);
	}

	@Override
	public List<TemporaryPhoto> findAllByTemporaryPostId(Long tempPostId) {
		return temporaryPhotoJpaRepository.findAllByTemporaryPostId(tempPostId).stream()
			.map(TemporaryPhotoEntity::toDomain)
			.toList();
	}

	@Override
	public List<TemporaryPhoto> findAllByTemporaryPostIdIn(List<Long> tempPostIds){
		return temporaryPhotoJpaRepository.findAllByTemporaryPostIdIn(tempPostIds).stream()
			.map(TemporaryPhotoEntity::toDomain)
			.toList();
	}

	@Override
	public void deleteAllByTemporaryPostId(Long tempPostId) {
		temporaryPhotoJpaRepository.deleteAllByTemporaryPostId(tempPostId);
	}

	@Override
	public void deleteAllByTemporaryPostIdIn(List<Long> tempPostIds) {
		temporaryPhotoJpaRepository.deleteAllByTemporaryPostIdIn(tempPostIds);
	}

}
