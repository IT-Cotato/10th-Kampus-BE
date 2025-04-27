package com.cotato.kampus.domain.post.implement.post;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.PostRepository;
import com.cotato.kampus.domain.post.domain.Post;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class PostDeleter {
	private final PostRepository postRepository;

	public void delete(Post post) {
		postRepository.delete(post);
	}

	public void deleteAllByBoardIds(List<Long> boardIds) {
		postRepository.deleteAllByBoardIdIn(boardIds);
	}
}
