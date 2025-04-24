package com.cotato.kampus.domain.post.implement.TemporaryPost;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.TemporaryPhotoRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class TemporaryPhotoDeleter {

	private final TemporaryPhotoRepository temporaryPhotoRepository;

	public void deleteAllByTempPostId(Long tempPostId) {
		temporaryPhotoRepository.deleteAllByTemporaryPostId(tempPostId);
	}

	public void deleteAllByTempPostIds(List<Long> tempPostIds) {
		temporaryPhotoRepository.deleteAllByTemporaryPostIdIn(tempPostIds);
	}
}
