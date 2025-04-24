package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.dao.entity.TemporaryPostEntity;
import com.cotato.kampus.domain.post.domain.TemporaryPost;
import com.cotato.kampus.domain.post.implement.port.TemporaryPostRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TemporaryPostRepositoryImpl implements TemporaryPostRepository {

	private final TemporaryPostJpaRepository temporaryPostJpaRepository;

	@Override
	public TemporaryPost save(TemporaryPost temporaryPost) {
		TemporaryPostEntity entity = TemporaryPostEntity.fromDomain(temporaryPost);
		return temporaryPostJpaRepository.save(entity).toDomain();
	}

	@Override
	public Optional<TemporaryPost> findById(Long id) {
		return temporaryPostJpaRepository.findById(id).map(TemporaryPostEntity::toDomain);
	}

	@Override
	public List<TemporaryPost> findAllByIdIn(List<Long> ids) {
		return temporaryPostJpaRepository.findAllByIdIn(ids).stream()
			.map(TemporaryPostEntity::toDomain)
			.toList();
	}

	@Override
	public void delete(TemporaryPost temporaryPost) {
		TemporaryPostEntity entity = TemporaryPostEntity.fromDomain(temporaryPost);
		temporaryPostJpaRepository.delete(entity);
	}

	@Override
	public void deleteAll(List<TemporaryPost> temporaryPosts) {
		List<TemporaryPostEntity> entities = temporaryPosts.stream()
			.map(TemporaryPostEntity::fromDomain)
			.toList();
		temporaryPostJpaRepository.deleteAll(entities);
	}

	@Override
	public void deleteAllByUserId(Long userId){
		temporaryPostJpaRepository.deleteAllByUserId(userId);
	}
}
