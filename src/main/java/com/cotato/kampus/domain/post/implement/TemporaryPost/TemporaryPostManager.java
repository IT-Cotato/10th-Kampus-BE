package com.cotato.kampus.domain.post.implement.TemporaryPost;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.domain.TemporaryPost;
import com.cotato.kampus.domain.post.implement.port.TemporaryPostRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class TemporaryPostManager {

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

	public TemporaryPost update(TemporaryPost temporaryPost, String title, String content) {
		TemporaryPost updatedTemporaryPost = temporaryPost.withUpdateInfo(title, content, Anonymity.ANONYMOUS);
		return temporaryPostRepository.save(updatedTemporaryPost);
	}

	public void delete(TemporaryPost temporaryPost){
		temporaryPostRepository.delete(temporaryPost);
	}

	public void deleteAllByIds(List<Long> tempPostIds) {
		temporaryPostRepository.deleteAllByIdIn(tempPostIds);
	}

	public void deleteAllByUser(Long userId) {
		temporaryPostRepository.deleteAllByUserId(userId);
	}
}
