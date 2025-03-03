package com.cotato.kampus.domain.support.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.support.domain.Inquiry;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

	Slice<Inquiry> findAllByUserIdOrderByCreatedTimeDesc(Long userId, Pageable pageable);
}
