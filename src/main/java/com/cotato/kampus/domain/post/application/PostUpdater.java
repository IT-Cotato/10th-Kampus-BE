package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostRepository;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.enums.PostCategory;
import com.cotato.kampus.domain.post.enums.PostStatus;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

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
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

		post.update(title, content, postCategory);
	}

	@Transactional
	public void increasePostLike(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

		post.increaseLikes();
	}

	public Long increaseNextAnonymousNumber(Long postId) {
		Post post = postFinder.getPost(postId);
		Long currentAnonymousNumber = post.getNextAnonymousNumber();

		post.increaseNextAnonymousNumber();
		postRepository.save(post);

		return currentAnonymousNumber;
	}

	@Transactional
	public void increaseScraps(Long postId){
		Post post = postFinder.getPost(postId);
		post.increaseScraps();

		postRepository.save(post);
	}

	@Transactional
	public void decreaseScraps(Long postId){
		Post post = postFinder.getPost(postId);
		post.decreaseScraps();

		postRepository.save(post);
	}

	@Transactional
	public void pendingPost(Long boardId){
		List<Post> posts = postRepository.findAllByBoardId(boardId);

		posts.forEach(post -> post.updateStatus(PostStatus.PENDING));
	}
}