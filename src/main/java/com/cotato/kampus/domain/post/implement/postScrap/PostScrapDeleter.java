package com.cotato.kampus.domain.post.implement.postScrap;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.domain.PostScrap;
import com.cotato.kampus.domain.post.implement.port.PostScrapRepository;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class PostScrapDeleter {

	private final PostScrapRepository postScrapRepository;

	public void delete(PostScrap postScrap) {
		postScrapRepository.delete(postScrap);
	}

	public void deleteAllByPostId(Long postId) {
		postScrapRepository.deleteAllByPostId(postId);
	}
}
