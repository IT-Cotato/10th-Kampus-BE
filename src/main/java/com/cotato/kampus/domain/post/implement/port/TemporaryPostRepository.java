package com.cotato.kampus.domain.post.implement.port;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.domain.TemporaryPost;

public interface TemporaryPostRepository {

	TemporaryPost save(TemporaryPost temporaryPost);

	Optional<TemporaryPost> findById(Long id);

	List<TemporaryPost> findAllByIdIn(List<Long> ids);

	List<TemporaryPost> findAllByUserId(Long userId);

	Slice<TemporaryPost> findAllByUserId(Long userId, Pageable pageable);

	void delete(TemporaryPost temporaryPost);

	void deleteAllByIdIn(List<Long> tempPostIds);

	void deleteAllByUserId(Long userId);
}
