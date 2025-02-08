package com.cotato.kampus.domain.user.application;

import static com.cotato.kampus.domain.post.application.PostFinder.*;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.admin.dto.StudentVerification;
import com.cotato.kampus.domain.university.application.UnivFinder;
import com.cotato.kampus.domain.university.domain.University;
import com.cotato.kampus.domain.user.dao.VerificationRecordRepository;
import com.cotato.kampus.domain.user.domain.VerificationRecord;
import com.cotato.kampus.global.common.dto.CustomPageRequest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationRecordFinder {

	private final VerificationRecordRepository verificationRecordRepository;
	private final UnivFinder univFinder;

	public Slice<StudentVerification> findAll(int page){
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);

		// VerificationRecord 조회
		Slice<VerificationRecord> records = verificationRecordRepository.findAllByOrderByCreatedTimeDesc(customPageRequest.of(SORT_PROPERTY));

		// VerificationRecord를 StudentVerification DTO로 변환
		return records.map(record -> {
			University university = univFinder.findUniversity(record.getUniversityId());
			return StudentVerification.of(record, university.getUniversityName());
		});
	}
}
