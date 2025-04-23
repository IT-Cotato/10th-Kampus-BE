package com.cotato.kampus.domain.post.implement.port;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.enums.PostStatus;

public interface CardNewsPostRepository {

	// 게시판의 게시글 상태로 조회
	Slice<Post> findAllByBoardIdAndPostStatus(Long boardId, PostStatus postStatus, Pageable pageable);
}
