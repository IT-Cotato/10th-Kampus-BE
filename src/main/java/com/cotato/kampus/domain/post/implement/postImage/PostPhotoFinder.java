package com.cotato.kampus.domain.post.implement.postImage;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.PostDraftPhotoRepository;
import com.cotato.kampus.domain.post.implement.port.PostPhotoRepository;
import com.cotato.kampus.domain.post.domain.PostDraftPhoto;
import com.cotato.kampus.domain.post.domain.PostPhoto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostPhotoFinder {

	private final PostPhotoRepository postPhotoRepository;

	public List<String> findPostPhotos(Long postId) {
		return postPhotoRepository.findALlByPostId(postId).stream()
			.map(PostPhoto::getPhotoUrl)
			.toList();
	}
}