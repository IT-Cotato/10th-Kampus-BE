package com.cotato.kampus.domain.post.implement.postCategory;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.PostCategoryRepository;
import com.cotato.kampus.domain.post.domain.PostCategory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCategoryDeleter {

	private final PostCategoryRepository postCategoryRepository;

	public void deleteAllByPostId(Long postId) {
		List<PostCategory> postCategories = postCategoryRepository.findByPostId(postId);

		postCategoryRepository.deleteAll(postCategories);
	}
}
