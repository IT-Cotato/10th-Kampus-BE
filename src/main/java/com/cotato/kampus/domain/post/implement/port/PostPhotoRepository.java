package com.cotato.kampus.domain.post.implement.port;

import java.util.List;
import java.util.Optional;

import com.cotato.kampus.domain.post.domain.PostPhoto;

public interface PostPhotoRepository{

	Optional<PostPhoto> findByPostIdAndOrder(Long postId, int order);

	List<PostPhoto> findAllByPostId(Long postId);

	List<PostPhoto> saveAll(List<PostPhoto> postPhotos);

	void deleteAllByPostId(Long postId);
}