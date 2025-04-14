package com.cotato.kampus.domain.post.implement.postSrcap;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.repository.PostScrapRepository;
import com.cotato.kampus.domain.post.domain.PostScrap;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapUpdater {

	private final PostScrapRepository postScrapRepository;
	private final PostScrapFinder postScrapFinder;

	public void append(Long postId, Long userId) {
		PostScrap postScrap = PostScrap.builder()
			.postId(postId)
			.userId(userId)
			.build();

		postScrapRepository.save(postScrap);
	}

	public void delete(Long postId, Long userId) {
		PostScrap postScrap = postScrapFinder.find(userId, postId);
		postScrapRepository.delete(postScrap);
	}

	public void deleteAllByPostId(Long postId) {
		List<PostScrap> postScraps = postScrapFinder.findAllByPostId(postId);
		postScrapRepository.deleteAll(postScraps);
	}
}
