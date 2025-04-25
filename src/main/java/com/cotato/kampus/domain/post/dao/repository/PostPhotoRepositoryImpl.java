package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.dao.entity.PostPhotoEntity;
import com.cotato.kampus.domain.post.domain.PostPhoto;
import com.cotato.kampus.domain.post.implement.port.PostPhotoRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostPhotoRepositoryImpl implements PostPhotoRepository {

	private final PostPhotoJpaRepository postPhotoJpaRepository;

	@Override
	public Optional<PostPhoto> findByPostIdAndOrder(Long postId, int order) {
		return postPhotoJpaRepository.findByPostIdAndOrder(postId, order)
			.map(PostPhotoEntity::toDomain);
	}

	@Override
	public List<PostPhoto> findAllByPostId(Long postId) {
		return postPhotoJpaRepository.findAllByPostId(postId).stream()
			.map(PostPhotoEntity::toDomain)
			.toList();
	}

	@Override
	public List<PostPhoto> saveAll(List<PostPhoto> postPhotos) {
		List<PostPhotoEntity> entities = postPhotos.stream()
			.map(PostPhotoEntity::fromDomain)
			.toList();

		return postPhotoJpaRepository.saveAll(entities).stream()
			.map(PostPhotoEntity::toDomain)
			.toList();
	}

	@Override
	public void deleteAllByPostId(Long postId) {
		postPhotoJpaRepository.deleteAllByPostId(postId);
	}

}
