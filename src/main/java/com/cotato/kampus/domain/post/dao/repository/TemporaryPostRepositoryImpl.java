package com.cotato.kampus.domain.post.dao.repository;

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
	public TemporaryPost save(TemporaryPost temporaryPost){
		TemporaryPostEntity entity = TemporaryPostEntity.fromDomain(temporaryPost);
		return temporaryPostJpaRepository.save(entity).toDomain();
	}
}
