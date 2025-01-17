package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostRepository;
import com.cotato.kampus.domain.post.domain.Post;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUpdater {

	private final PostRepository postRepository;
	private final PostFinder postFinder;

	public Long increaseNextAnonymousNumber(Long postId){

		Post post = postFinder.getById(postId);
		Long currentAnonymousNumber = post.getNextAnonymousNumber();

		post.increaseNextAnonymousNumber();
		postRepository.save(post);

		return currentAnonymousNumber;
	}

}
