package com.cotato.kampus.domain.post.implement.postImage;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.PostPhotoRepository;
import com.cotato.kampus.domain.post.domain.PostPhoto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostPhotoFinder {

	private final PostPhotoRepository postPhotoRepository;

	public String findFirstPhoto(Long postId) {
		return postPhotoRepository.findByPostIdAndOrder(postId, 0)
			.map(PostPhoto::getPhotoUrl)
			.orElse(null);
	}

	public List<String> findPostPhotos(Long postId) {
		return postPhotoRepository.findAllByPostId(postId).stream()
			.map(PostPhoto::getPhotoUrl)
			.toList();
	}
}