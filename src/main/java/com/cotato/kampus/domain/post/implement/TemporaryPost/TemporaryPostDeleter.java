package com.cotato.kampus.domain.post.implement.TemporaryPost;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.domain.TemporaryPost;
import com.cotato.kampus.domain.post.implement.port.TemporaryPostRepository;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class TemporaryPostDeleter {

	private final TemporaryPostRepository temporaryPostRepository;

	public void delete(TemporaryPost temporaryPost){
		temporaryPostRepository.delete(temporaryPost);
	}

	public void deleteAll(List<TemporaryPost> temporaryPosts) {
		temporaryPostRepository.deleteAll(temporaryPosts);
	}

	public void deleteAllByUser(Long userId) {
		temporaryPostRepository.deleteAllByUserId(userId);
	}
}
