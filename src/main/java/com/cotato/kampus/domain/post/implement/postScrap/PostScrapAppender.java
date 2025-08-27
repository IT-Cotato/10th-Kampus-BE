package com.cotato.kampus.domain.post.implement.postScrap;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.domain.PostScrap;
import com.cotato.kampus.domain.post.implement.port.PostScrapRepository;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
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
