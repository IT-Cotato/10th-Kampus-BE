package com.cotato.kampus.domain.post.dao.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.dao.entity.PostScrapEntity;
import com.cotato.kampus.domain.post.domain.PostScrap;
import com.cotato.kampus.domain.post.implement.port.PostScrapRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostScrapRepositoryImpl implements PostScrapRepository {

	private final PostScrapJpaRepository postScrapJpaRepository;

	@Override
	public PostScrap save(PostScrap postScrap) {
		PostScrapEntity entity = PostScrapEntity.fromDomain(postScrap);
		return postScrapJpaRepository.save(entity).toDomain();
	}

	@Override
	public void delete(PostScrap postScrap) {
		PostScrapEntity entity = PostScrapEntity.fromDomain(postScrap);
		postScrapJpaRepository.delete(entity);
	}

	@Override
	public void deleteAllByPostId(Long postId) {
		postScrapJpaRepository.deleteAllByPostId(postId);
	}

	@Override
	public boolean existsByPostIdAndUserId(Long postId, Long userId) {
		return postScrapJpaRepository.existsByPostIdAndUserId(postId, userId);
	}

	@Override
	public Optional<PostScrap> findByPostIdAndUserId(Long postId, Long userId){
		return postScrapJpaRepository.findByPostIdAndUserId(postId, userId)
			.map(PostScrapEntity::toDomain);
	}

	@Override
	public Slice<PostScrap> findAllByUserId(Long userId, Pageable pageable) {
		return postScrapJpaRepository.findAllByUserId(userId, pageable)
			.map(PostScrapEntity::toDomain);
	}
}
