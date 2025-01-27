package com.cotato.kampus.domain.post.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.domain.PostDraft;

public interface PostDraftRepository extends JpaRepository<PostDraft, Long> {
}
