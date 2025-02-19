package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostRepository;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.enums.PostCategory;
import com.cotato.kampus.domain.post.enums.PostStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUpdater {

	private final PostRepository postRepository;
	private final PostFinder postFinder;

	@Transactional
	public void updatePost(Long postId, String title, String content, PostCategory postCategory) {
		Post post = postFinder.getPost(postId);

		post.update(title, content, postCategory);
	}

	@Transactional
	public void increasePostLike(Long postId) {
		Post post = postFinder.getPost(postId);

		post.increaseLikes();
	}

	@Transactional
	public void decreasePostLike(Long postId) {
		Post post = postFinder.getPost(postId);

		post.decreaseLikes();
	}

	@Transactional
	public Long increaseNextAnonymousNumber(Long postId) {
		Post post = postFinder.getPost(postId);
		Long currentAnonymousNumber = post.getNextAnonymousNumber();

		post.increaseNextAnonymousNumber();

		return currentAnonymousNumber;
	}

	@Transactional
	public void increaseScraps(Long postId) {
		Post post = postFinder.getPost(postId);

		post.increaseScraps();
	}

	@Transactional
	public void decreaseScraps(Long postId) {
		Post post = postFinder.getPost(postId);

		post.decreaseScraps();
	}

	@Transactional
	public void pendingPost(Long boardId) {
		List<Post> posts = postRepository.findAllByBoardId(boardId);

		posts.forEach(post -> post.updateStatus(PostStatus.PENDING));
	}

	@Transactional
	public void revertPendingPosts(Long boardId) {
		List<Post> posts = postRepository.findAllByBoardId(boardId);

		posts.forEach(post -> post.updateStatus(PostStatus.PUBLISHED));
	}
}