package com.cotato.kampus.domain.post.implement.post;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.domain.CardNewsPost;
import com.cotato.kampus.domain.post.domain.NormalPost;
import com.cotato.kampus.domain.post.enums.PostStatus;
import com.cotato.kampus.domain.post.implement.port.PostRepository;
import com.cotato.kampus.domain.post.domain.Post;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class PostAppender {

	private final PostRepository postRepository;

	public Post appendNormalPost(
		Long userId,
		Long boardId,
		String title,
		String content
	) {
		Post post = NormalPost.create(boardId, userId, title, content, PostStatus.PUBLISHED, Anonymity.ANONYMOUS);
		return postRepository.save(post);
	}

	public Post appendCardNewsPost(
		Long userId,
		Long boardId,
		String title,
		String content
	) {
		Post post = CardNewsPost.create(boardId, userId, title, content, PostStatus.PUBLISHED, Anonymity.ANONYMOUS);
		return postRepository.save(post);
	}
}
