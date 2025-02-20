package com.cotato.kampus.domain.notice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.notice.domain.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}