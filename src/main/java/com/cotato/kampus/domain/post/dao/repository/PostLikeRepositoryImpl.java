package com.cotato.kampus.domain.post.dao.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.dao.entity.PostLikeEntity;
import com.cotato.kampus.domain.post.domain.PostLike;
import com.cotato.kampus.domain.post.implement.port.PostLikeRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostLikeRepositoryImpl implements PostLikeRepository {

	private final PostLikeJpaRepository postLikeJpaRepository;

	@Override
	public PostLike save(PostLike postLike) {
		PostLikeEntity entity = PostLikeEntity.fromDomain(postLike);
		return postLikeJpaRepository.save(entity).toDomain();
	}

	@Override
	public void delete(PostLike postLike) {
		PostLikeEntity entity = PostLikeEntity.fromDomain(postLike);
		postLikeJpaRepository.delete(entity);
	}

	@Override
	public boolean existsByPostIdAndUserId(Long postId, Long userId) {
		return postLikeJpaRepository.existsByPostIdAndUserId(postId, userId);
	}

	@Override
	public Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId) {
		return postLikeJpaRepository.findByPostIdAndUserId(postId, userId)
			.map(PostLikeEntity::toDomain);
	}

	@Override
	public void deleteAllByPostId(Long postId) {
		postLikeJpaRepository.deleteAllByPostId(postId);
	}

}
