package com.cotato.kampus.domain.post.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.domain.PostDraft;

public interface PostDraftRepository extends JpaRepository<PostDraft, Long> {

	Slice<PostDraft> findAllByBoardIdAndUserIdOrderByCreatedTimeDesc(Long postId, Long userId, Pageable pageable);

	List<PostDraft> findAllByBoardIdAndUserId(Long boardId, Long userId);
}
