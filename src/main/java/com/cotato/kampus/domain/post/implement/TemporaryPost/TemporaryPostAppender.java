package com.cotato.kampus.domain.post.implement.TemporaryPost;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.domain.TemporaryPost;
import com.cotato.kampus.domain.post.implement.port.TemporaryPostRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class TemporaryPostAppender {

	private final TemporaryPostRepository temporaryPostRepository;

	public TemporaryPost append(Long userId, Long boardId, String title, String content) {
		TemporaryPost temporaryPost = TemporaryPost.builder()
			.userId(userId)
			.boardId(boardId)
			.title(title)
			.content(content)
			.anonymity(Anonymity.ANONYMOUS)
			.build();

		return temporaryPostRepository.save(temporaryPost);
	}
}
