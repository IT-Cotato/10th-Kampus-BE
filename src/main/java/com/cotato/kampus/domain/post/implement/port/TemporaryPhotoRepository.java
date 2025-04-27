package com.cotato.kampus.domain.post.implement.port;

import java.util.List;
import java.util.Optional;

import com.cotato.kampus.domain.post.domain.TemporaryPhoto;

public interface TemporaryPhotoRepository {

	List<TemporaryPhoto> saveAll(List<TemporaryPhoto> temporaryPhotos);

	Optional<TemporaryPhoto> findByTemporaryPostIdAndOrder(Long tempPostId, int order);

	List<TemporaryPhoto> findAllByTemporaryPostIdOrderByOrderAsc(Long tempPostId);

	List<TemporaryPhoto> findAllByTemporaryPostIdInOrderByOrderAsc(List<Long> tempPostIds);

	void deleteAllByTemporaryPostId(Long tempPostId);

	void deleteAllByTemporaryPostIdIn(List<Long> tempPostIds);
}
