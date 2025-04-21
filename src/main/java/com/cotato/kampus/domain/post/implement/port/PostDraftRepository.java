package com.cotato.kampus.domain.post.implement.port;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.domain.PostDraft;

public interface PostDraftRepository extends JpaRepository<PostDraft, Long> {

	Slice<PostDraft> findAllByUserIdOrderByCreatedTimeDesc(Long userId, Pageable pageable);

	List<PostDraft> findAllByUserId(Long userId);

	int countByUserId(Long userId);
}
