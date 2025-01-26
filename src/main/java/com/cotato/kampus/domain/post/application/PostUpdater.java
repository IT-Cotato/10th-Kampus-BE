package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.dao.PostRepository;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.enums.PostCategory;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
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

	@Transactional
	public void updatePost(Long postId, String title, String content, PostCategory postCategory, Anonymity anonymity) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

		post.update(title, content, postCategory, anonymity);
	}

	public Long increaseNextAnonymousNumber(Long postId){

		Post post = postFinder.getPost(postId);
		Long currentAnonymousNumber = post.getNextAnonymousNumber();

		post.increaseNextAnonymousNumber();
		postRepository.save(post);

		return currentAnonymousNumber;
	}

}
