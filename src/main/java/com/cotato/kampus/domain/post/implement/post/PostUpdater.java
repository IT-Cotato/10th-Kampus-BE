package com.cotato.kampus.domain.post.implement.post;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.implement.port.PostRepository;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.enums.PostStatus;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class PostUpdater {

	private final PostRepository postRepository;

	public Post update(Post post, String title, String content) {
		Post updatedPost = post.withUpdateInfo(title, content, Anonymity.ANONYMOUS);
		return postRepository.save(updatedPost);
	}

	public Post increaseLikeCount(Post post) {
		Post updatedPost = post.increaseLikeCount();
		return postRepository.save(updatedPost);
	}

	public Post decreaseLikeCount(Post post) {
		Post updatedPost = post.decreaseLikeCount();
		return postRepository.save(updatedPost);
	}

	public Post increaseCommentCount(Post post){
		Post updatedPost = post.increaseCommentCount();
		return postRepository.save(updatedPost);
	}

	public Post decreaseCommentCount(Post post){
		Post updatedPost = post.decreaseCommentCount();
		return postRepository.save(updatedPost);
	}

	public Post increaseCommentAndAnonymousCount(Post post) {
		Post updatedPost = post.increaseCommentAndAnonymousCount();
		return postRepository.save(updatedPost);
	}

	public Post increaseScrapCount(Post post) {
		Post updatedPost = post.increaseScrapCount();
		return postRepository.save(updatedPost);
	}

	public Post decreaseScrapCount(Post post) {
		Post updatedPost = post.decreaseScrapCount();
		return postRepository.save(updatedPost);
	}

	public int pendingAllByBoardId(Long boardId) {
		return postRepository.updateStatusByBoardIdAndCurrentStatus(boardId, PostStatus.PUBLISHED, PostStatus.PENDING);
	}

	public int revertPendingAllByBoardId(Long boardId) {
		return postRepository.updateStatusByBoardIdAndCurrentStatus(boardId, PostStatus.PENDING, PostStatus.PUBLISHED);
	}
}