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
	public void append(Long userId, Long universityId, String imageUrl){
		VerificationPhoto verificationPhoto = VerificationPhoto.builder()
			.userId(userId)
			.universityId(universityId)
			.imageUrl(imageUrl)
			.build();

		verificationPhotoRepository.save(verificationPhoto);
	}
}
