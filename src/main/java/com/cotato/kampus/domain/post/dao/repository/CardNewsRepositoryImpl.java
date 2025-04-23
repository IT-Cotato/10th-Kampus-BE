package com.cotato.kampus.domain.post.dao.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.dao.entity.CardNewsPostEntity;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.enums.PostStatus;
import com.cotato.kampus.domain.post.implement.port.CardNewsPostRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CardNewsRepositoryImpl implements CardNewsPostRepository {

	private final CardNewsPostJpaRepository cardNewsPostJpaRepository;

	@Override
	public Slice<Post> findAllByBoardIdAndPostStatus(Long boardId, PostStatus postStatus, Pageable pageable){
		return cardNewsPostJpaRepository.findAllByBoardIdAndPostStatus(boardId, postStatus, pageable)
			.map(CardNewsPostEntity::toDomain);
	}
}
