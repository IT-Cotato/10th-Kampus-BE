package com.cotato.kampus.domain.post.implement.port;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.domain.PostScrap;

public interface PostScrapRepository {

	PostScrap save(PostScrap postScrap);

	void delete(PostScrap postScrap);

	void deleteAllByPostId(Long postId);

	boolean existsByPostIdAndUserId(Long postId, Long userId);

	Optional<PostScrap> findByPostIdAndUserId(Long postId, Long userId);

	Slice<PostScrap> findAllByUserId(Long userId, Pageable pageable);
}
