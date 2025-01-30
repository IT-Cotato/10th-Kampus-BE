package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostDraftPhotoRepository;
import com.cotato.kampus.domain.post.dao.PostPhotoRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImageDeleter {

	private final PostDraftPhotoRepository postDraftPhotoRepository;
	private final PostPhotoRepository postPhotoRepository;

	public void deletePostDraftPhotos(List<String> imageUrls){
		postDraftPhotoRepository.deleteByPhotoUrlIn(imageUrls);
	}

	public void deletePostPhotos(Long postId){
		postPhotoRepository.deleteAllByPostId(postId);
	}
}
