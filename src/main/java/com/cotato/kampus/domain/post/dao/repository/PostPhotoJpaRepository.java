package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.dao.entity.PostPhotoEntity;

public interface PostPhotoJpaRepository extends JpaRepository<PostPhotoEntity, Long> {

	Optional<PostPhotoEntity> findByPostIdAndOrder(Long postId, int order);

	List<PostPhotoEntity> findAllByPostId(Long postId);

	List<PostPhotoEntity> saveAll(List<PostPhotoEntity> entities);

	void deleteAllByPostId(Long postId);
}
