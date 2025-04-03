package com.cotato.kampus.domain.post.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.domain.PostDraftCategory;

@Repository
public interface PostDraftCategoryRepository extends JpaRepository<PostDraftCategory, Long> {

	List<PostDraftCategory> findByPostDraftIdIn(List<Long> postDraftIds);

	List<PostDraftCategory> findByPostDraftId(Long postDraftId);
}
