package com.cotato.kampus.domain.post.implement.TemporaryPost;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.domain.TemporaryPost;
import com.cotato.kampus.domain.post.implement.port.TemporaryPostRepository;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class TemporaryPostUpdater {

	private final TemporaryPostRepository temporaryPostRepository;

	public TemporaryPost update(TemporaryPost temporaryPost, String title, String content) {
		TemporaryPost updatedTemporaryPost = temporaryPost.withUpdateInfo(title, content, Anonymity.ANONYMOUS);
		return temporaryPostRepository.save(updatedTemporaryPost);
	}
}
