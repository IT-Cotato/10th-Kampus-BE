package com.cotato.kampus.domain.post.dao.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.dao.entity.NormalPostEntity;
import com.cotato.kampus.domain.post.enums.PostStatus;

public interface NormalPostJpaRepository extends JpaRepository<NormalPostEntity, Long> {

	Slice<NormalPostEntity> findAllByUserId(Long userId, Pageable pageable);

	Slice<NormalPostEntity> findAllByBoardIdAndPostStatus(Long boardId, PostStatus postStatus, Pageable pageable);

}
