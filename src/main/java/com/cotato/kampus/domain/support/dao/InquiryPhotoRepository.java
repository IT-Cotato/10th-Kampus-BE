package com.cotato.kampus.domain.support.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.support.domain.InquiryPhoto;

@Repository
public interface InquiryPhotoRepository extends JpaRepository<InquiryPhoto, Long> {

	List<InquiryPhoto> findAllByInquiryId(Long inquiryId);
}
