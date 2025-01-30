package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostDraftPhotoRepository;
import com.cotato.kampus.domain.post.dao.PostPhotoRepository;
import com.cotato.kampus.domain.post.domain.PostDraftPhoto;
import com.cotato.kampus.domain.post.domain.PostPhoto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostImageFinder {

	private final PostPhotoRepository postPhotoRepository;
	private final PostDraftPhotoRepository postDraftPhotoRepository;

	public List<String> findPostPhotos(Long postId) {
		return postPhotoRepository.findALlByPostId(postId).stream()
			.map(PostPhoto::getPhotoUrl)
			.toList();
	}

	public List<String> findAllDraftPhotos(List<Long> postDraftIds){
		return postDraftPhotoRepository.findAllByPostDraftIdIn(postDraftIds)
			.stream()
			.map(PostDraftPhoto::getPhotoUrl)
			.toList();
	}
}