package com.cotato.kampus.domain.post.implement.port;

import java.util.List;
import java.util.Optional;

import com.cotato.kampus.domain.post.domain.PostScrap;

public interface PostScrapRepository {

	PostScrap save(PostScrap postScrap);

	void delete(PostScrap postScrap);

	void deleteAllByPostId(Long postId);

	boolean existsByPostIdAndUserId(Long postId, Long userId);

	Optional<PostScrap> findByPostIdAndUserId(Long postId, Long userId);

	List<Long> findAllPostIdsByUserId(Long userId);
}
