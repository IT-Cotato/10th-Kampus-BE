package com.cotato.kampus.domain.post.dao.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.dao.entity.CardNewsPostEntity;
import com.cotato.kampus.domain.post.enums.PostStatus;

public interface CardNewsPostJpaRepository extends JpaRepository<CardNewsPostEntity, Long> {

	Slice<CardNewsPostEntity> findAllByBoardIdAndPostStatus(Long boardId, PostStatus postStatus, Pageable pageable);

}
