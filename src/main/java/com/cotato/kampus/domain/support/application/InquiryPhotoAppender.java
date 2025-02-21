package com.cotato.kampus.domain.support.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.support.dao.InquiryPhotoRepository;
import com.cotato.kampus.domain.support.domain.InquiryPhoto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryPhotoAppender {

	private final InquiryPhotoRepository inquiryPhotoRepository;

	@Transactional
	public void appendAll(Long inquiryId, List<String> photoUrls) {
		photoUrls.forEach(
			photoUrl -> {
				InquiryPhoto inquiryPhoto = InquiryPhoto.builder()
					.inquiryId(inquiryId)
					.photoUrl(photoUrl)
					.build();

				inquiryPhotoRepository.save(inquiryPhoto);
			}
		);
	}
}
