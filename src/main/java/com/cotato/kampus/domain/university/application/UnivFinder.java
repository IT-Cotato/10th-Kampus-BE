package com.cotato.kampus.domain.university.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.university.dao.UniversityRepository;
import com.cotato.kampus.domain.university.domain.University;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UnivFinder {

	private final UniversityRepository universityRepository;

	public Long findUniversityId(String universityName) {
		University university =  universityRepository.findByUniversityName(universityName)
			.orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));

		return university.getId();
	}

	public University findUniversity(Long universityId){
		return universityRepository.findById(universityId)
			.orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));
	}

}
