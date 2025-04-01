package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostCategoryRepository;
import com.cotato.kampus.domain.post.dao.PostDraftCategoryRepository;
import com.cotato.kampus.domain.post.domain.PostCategory;
import com.cotato.kampus.domain.post.domain.PostDraftCategory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCategoryDeleter {

	private final PostCategoryRepository postCategoryRepository;
	private final PostDraftCategoryRepository postDraftCategoryRepository;

	public void deleteAllByPostId(Long postId){
		List<PostCategory> postCategories = postCategoryRepository.findByPostId(postId);

		postCategoryRepository.deleteAll(postCategories);
	}

	public void deleteAllByPostDraftIds(List<Long> postDraftIds){
		List<PostDraftCategory> postDraftCategories = postDraftCategoryRepository.findByPostDraftIdIn(postDraftIds);

		postDraftCategoryRepository.deleteAll(postDraftCategories);
	}

	public void deleteAllByPostDraftId(Long postDraftId){
		List<PostDraftCategory> postDraftCategories = postDraftCategoryRepository.findByPostDraftId(postDraftId);

		postDraftCategoryRepository.deleteAll(postDraftCategories);
	}
}
