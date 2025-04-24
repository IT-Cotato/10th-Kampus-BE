package com.cotato.kampus.domain.post.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.post.dao.entity.TemporaryPostEntity;

public interface TemporaryPostJpaRepository extends JpaRepository<TemporaryPostEntity, Long> {

}
