package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostDraftRepository;
import com.cotato.kampus.domain.post.dao.PostRepository;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostDraft;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDeleter {
	private final PostRepository postRepository;
	private final PostFinder postFinder;
	private final PostDraftRepository postDraftRepository;

	public void delete(Long postId){
		Post post = postFinder.getPost(postId);
		postRepository.delete(post);
	}

	public void deleteDraft(Long postDraftId){
		PostDraft postDraft = postFinder.findPostDraft(postDraftId);
		postDraftRepository.delete(postDraft);
	}
}
