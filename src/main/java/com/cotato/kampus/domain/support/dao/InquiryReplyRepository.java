package com.cotato.kampus.domain.support.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.support.domain.InquiryReply;

@Repository
public interface InquiryReplyRepository extends JpaRepository<InquiryReply, Long> {

	Optional<InquiryReply> findByInquiryId(Long inquiryId);
}
