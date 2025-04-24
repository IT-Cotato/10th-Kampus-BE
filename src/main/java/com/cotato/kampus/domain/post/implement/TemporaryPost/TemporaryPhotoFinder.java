package com.cotato.kampus.domain.post.implement.TemporaryPost;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.domain.TemporaryPhoto;
import com.cotato.kampus.domain.post.implement.port.TemporaryPhotoRepository;

import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TemporaryPhotoFinder {

	private final TemporaryPhotoRepository temporaryPhotoRepository;

	public String findFirstPhoto(Long tempPostId) {
		return temporaryPhotoRepository.findByTemporaryPostIdAndOrder(tempPostId, 0)
			.map(TemporaryPhoto::getPhotoUrl)
			.orElse(null);
	}

	public List<String> findAllPhotos(Long tempPostId) {
		return temporaryPhotoRepository.findAllByTemporaryPostIdOrderByOrderAsc(tempPostId).stream()
			.map(TemporaryPhoto::getPhotoUrl)
			.toList();
	}

	public List<String> findAllPhotos(List<Long> tempPostIds) {
		return temporaryPhotoRepository.findAllByTemporaryPostIdInOrderByOrderAsc(tempPostIds).stream()
			.map(TemporaryPhoto::getPhotoUrl)
			.toList();
	}
}
