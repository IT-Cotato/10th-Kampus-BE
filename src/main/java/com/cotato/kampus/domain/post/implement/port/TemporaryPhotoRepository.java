package com.cotato.kampus.domain.post.implement.port;

import java.util.List;
import java.util.Optional;

import com.cotato.kampus.domain.post.domain.TemporaryPhoto;

public interface TemporaryPhotoRepository {

	Optional<TemporaryPhoto> findFirstByTemporaryPostIdOrderByCreatedTimeAsc(Long tempPostId);

	List<TemporaryPhoto> findAllByTemporaryPostId(Long tempPostId);

	List<TemporaryPhoto> findAllByTemporaryPostIdIn(List<Long> tempPostIds);

	void deleteAllByTemporaryPostId(Long tempPostId);

	void deleteAllByTemporaryPostIdIn(List<Long> tempPostIds);
}
