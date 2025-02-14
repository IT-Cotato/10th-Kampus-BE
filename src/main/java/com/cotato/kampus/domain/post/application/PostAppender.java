package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.dao.PostDraftRepository;
import com.cotato.kampus.domain.post.domain.PostDraft;
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
	private final PostDraftRepository postDraftRepository;

	@Transactional
	public Long append(
		Long userId,
		Long boardId,
		String title,
		String content,
		PostCategory postCategory
	){
		Post post = Post.builder()
			.userId(userId)
			.boardId(boardId)
			.title(title)
			.content(content)
			.likes(0L)
			.scraps(0L)
			.comments(0L)
			.anonymity(Anonymity.ANONYMOUS)
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
		PostCategory postCategory
	){
		Long userId = apiUserResolver.getUserId();

		PostDraft postDraft = PostDraft.builder()
			.userId(userId)
			.boardId(boardId)
			.title(title)
			.content(content)
			.postCategory(postCategory)
			.build();

		return postDraftRepository.save(postDraft).getId();
	}

	@Transactional
	public Long appendCardNews(
		Long userId,
		Long boardId,
		String title
	){
		Post post = Post.builder()
			.userId(userId)
			.boardId(boardId)
			.title(title)
			.anonymity(Anonymity.IDENTIFIED)
			.postStatus(PostStatus.PUBLISHED)
			.build();

		return postRepository.save(post).getId();
	}
}
