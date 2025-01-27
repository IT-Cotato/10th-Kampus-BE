package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.enums.PostCategory;
import com.cotato.kampus.domain.post.enums.PostStatus;
import com.cotato.kampus.domain.post.dao.PostRepository;
import com.cotato.kampus.domain.post.domain.Post;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostAppender {

	private final ApiUserResolver apiUserResolver;
	private final PostRepository postRepository;

	@Transactional
	public Long append(
		Long boardId,
		String title,
		String content,
		PostCategory postCategory,
		Anonymity anonymity
	){

		Long userId = apiUserResolver.getUserId();

		Post post = Post.builder()
			.userId(userId)
			.boardId(boardId)
			.title(title)
			.content(content)
			.likes(0L)
			.scraps(0L)
			.comments(0L)
			.anonymity(anonymity)
			.postStatus(PostStatus.PUBLISHED)
			.postCategory(postCategory)
			.nextAnonymousNumber(1L)
			.build();

		return postRepository.save(post).getId();
	}

	@Transactional
	public Long draft(
		Long boardId,
		String title,
		String content,
		PostCategory postCategory,
		Anonymity anonymity
	){
		Long userId = apiUserResolver.getUserId();

		Post post = Post.builder()
			.userId(userId)
			.boardId(boardId)
			.title(title)
			.content(content)
			.anonymity(anonymity)
			.postStatus(PostStatus.DRAFT)
			.postCategory(postCategory)
			.build();

		return postRepository.save(post).getId();
	}
}
