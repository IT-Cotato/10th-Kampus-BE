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
	public List<TemporaryPhoto> saveAll(List<TemporaryPhoto> temporaryPhotos){
		List<TemporaryPhotoEntity> entities = temporaryPhotos.stream()
			.map(TemporaryPhotoEntity::fromDomain)
			.toList();

		return temporaryPhotoJpaRepository.saveAll(entities).stream()
			.map(TemporaryPhotoEntity::toDomain)
			.toList();
	}

	@Override
	public Optional<TemporaryPhoto> findByTemporaryPostIdAndOrder(Long tempPostId, int order) {
		return temporaryPhotoJpaRepository.findByTemporaryPostIdAndOrder(tempPostId, order)
			.map(TemporaryPhotoEntity::toDomain);
	}

	@Override
	public List<TemporaryPhoto> findAllByTemporaryPostIdOrderByOrderAsc(Long tempPostId) {
		return temporaryPhotoJpaRepository.findAllByTemporaryPostIdOrderByOrderAsc(tempPostId).stream()
			.map(TemporaryPhotoEntity::toDomain)
			.toList();
	}

	@Override
	public List<TemporaryPhoto> findAllByTemporaryPostIdInOrderByOrderAsc(List<Long> tempPostIds){
		return temporaryPhotoJpaRepository.findAllByTemporaryPostIdInOrderByOrderAsc(tempPostIds).stream()
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
