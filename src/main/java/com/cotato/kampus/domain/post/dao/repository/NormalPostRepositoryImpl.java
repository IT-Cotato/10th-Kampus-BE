package com.cotato.kampus.domain.post.dao.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.dao.entity.NormalPostEntity;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.enums.PostStatus;
import com.cotato.kampus.domain.post.implement.port.NormalPostRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NormalPostRepositoryImpl implements NormalPostRepository {

	private final NormalPostJpaRepository normalPostJpaRepository;

	@Override
	public Slice<Post> findAllByUserId(Long userId, Pageable pageable){
		return normalPostJpaRepository.findAllByUserId(userId, pageable)
			.map(NormalPostEntity::toDomain);
	}

	@Override
	public Slice<Post> findAllByBoardIdAndPostStatus(Long boardId, PostStatus postStatus, Pageable pageable) {
		return normalPostJpaRepository.findAllByBoardIdAndPostStatus(boardId, postStatus, pageable)
			.map(NormalPostEntity::toDomain);
	}
}
