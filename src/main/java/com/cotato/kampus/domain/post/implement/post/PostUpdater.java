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

	public void increaseLikeCount(Post post) {
		Post updatedPost = post.increaseLikeCount();
		postRepository.save(updatedPost);
	}

	public void decreaseLikeCount(Post post) {
		Post updatedPost = post.decreaseLikeCount();
		postRepository.save(updatedPost);
	}

	public void increaseCommentCount(Post post){
		Post updatedPost = post.increaseCommentCount();
		postRepository.save(updatedPost);
	}

	public void decreaseCommentCount(Post post){
		Post updatedPost = post.decreaseCommentCount();
		postRepository.save(updatedPost);
	}

	public void increaseAnonymousCount(Post post) {
		Post updatedPost = post.increaseAnonymousCount();
		postRepository.save(updatedPost);
	}

	public void increaseScrapCount(Post post) {
		Post updatedPost = post.increaseScrapCount();
		postRepository.save(updatedPost);
	}

	public void decreaseScrapCount(Post post) {
		Post updatedPost = post.decreaseScrapCount();
		postRepository.save(updatedPost);
	}

	public void pendingAllByBoardId(Long boardId) {
		postRepository.updateStatusByBoardIdAndCurrentStatus(boardId, PostStatus.PUBLISHED, PostStatus.PENDING);
	}

	public void revertPendingAllByBoardId(Long boardId) {
		postRepository.updateStatusByBoardIdAndCurrentStatus(boardId, PostStatus.PENDING, PostStatus.PUBLISHED);
	}
}