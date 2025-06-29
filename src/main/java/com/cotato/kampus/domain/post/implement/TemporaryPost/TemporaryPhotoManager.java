package com.cotato.kampus.domain.post.implement.TemporaryPost;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.domain.TemporaryPhoto;
import com.cotato.kampus.domain.post.implement.port.TemporaryPhotoRepository;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class TemporaryPhotoManager {

	private final TemporaryPhotoRepository temporaryPhotoRepository;

	public List<TemporaryPhoto> appendAll(Long tempPostId, List<String> photoUrls) {
		List<TemporaryPhoto> tempPhotos = IntStream.range(0, photoUrls.size())
				.mapToObj(i -> TemporaryPhoto.builder()
					.temporaryPostId(tempPostId)
					.photoUrl(photoUrls.get(i))
					.order(i)
					.build())
				.toList();

		return temporaryPhotoRepository.saveAll(tempPhotos);
	}

	public void deleteAllByTempPostId(Long tempPostId) {
		temporaryPhotoRepository.deleteAllByTemporaryPostId(tempPostId);
	}

	public void deleteAllByTempPostIds(List<Long> tempPostIds) {
		temporaryPhotoRepository.deleteAllByTemporaryPostIdIn(tempPostIds);
	}
}
