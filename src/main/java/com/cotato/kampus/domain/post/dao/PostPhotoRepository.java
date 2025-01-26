package com.cotato.kampus.domain.post.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.domain.PostPhoto;

@Repository
public interface PostPhotoRepository extends JpaRepository<PostPhoto, Long> {

	Optional<PostPhoto> findFirstByPostIdOrderByCreatedTimeDesc(Long postId);

	Optional<PostPhoto> findFirstByPostIdOrderByCreatedTimeAsc(Long postId);

	List<PostPhoto> findALlByPostId(Long postId);

	void deleteAllByPostId(Long postId);
}