package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostRepository;
import com.cotato.kampus.domain.post.dao.PostScrapRepository;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostScrap;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapAppender {

	private final PostScrapRepository postScrapRepository;

	public void append(Long postId, Long userId) {
		PostScrap postScrap = PostScrap.builder()
			.postId(postId)
			.userId(userId)
			.build();

		postScrapRepository.save(postScrap);
	}
}
