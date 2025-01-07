package com.cotato.kampus.domain.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.auth.domain.RefreshEntity;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

	void deleteByRefresh(String refresh);
}
