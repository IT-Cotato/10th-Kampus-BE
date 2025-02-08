package com.cotato.kampus.domain.user.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.user.dao.VerificationPhotoRepository;
import com.cotato.kampus.domain.user.domain.VerificationPhoto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationPhotoAppender {

	private final VerificationPhotoRepository verificationPhotoRepository;

	@Transactional
	public void append(Long verificationRecordId, String imageUrl){
		VerificationPhoto verificationPhoto = VerificationPhoto.builder()
			.imageUrl(imageUrl)
			.verificationRecordId(verificationRecordId)
			.build();

		verificationPhotoRepository.save(verificationPhoto);
	}
}
