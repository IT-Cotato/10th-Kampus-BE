package com.cotato.kampus.domain.post.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.domain.PostDraftPhoto;

public interface PostDraftPhotoRepository extends JpaRepository<PostDraftPhoto, Long> {

	Optional<PostDraftPhoto> findFirstByPostDraftIdOrderByCreatedTimeDesc(Long postDraftId);

	List<PostDraftPhoto> findAllByPostDraftIdIn(List<Long> postDraftId);

	void deleteByPhotoUrlIn(List<String> photoUrls);
}
