package com.cotato.kampus.domain.post.implement.port;

import java.util.List;
import java.util.Optional;

import com.cotato.kampus.domain.post.domain.TemporaryPost;

public interface TemporaryPostRepository {

	TemporaryPost save(TemporaryPost temporaryPost);

	Optional<TemporaryPost> findById(Long id);

	List<TemporaryPost> findAllByIdIn(List<Long> ids);

	void delete(TemporaryPost temporaryPost);

	void deleteAll(List<TemporaryPost> temporaryPosts);

	void deleteAllByUserId(Long userId);
}
