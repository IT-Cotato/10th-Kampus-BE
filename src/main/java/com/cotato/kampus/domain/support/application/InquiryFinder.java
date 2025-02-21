package com.cotato.kampus.domain.support.application;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.support.dao.InquiryRepository;
import com.cotato.kampus.domain.support.domain.Inquiry;
import com.cotato.kampus.domain.support.dto.InquiryDto;
import com.cotato.kampus.domain.support.dto.InquiryPreview;
import com.cotato.kampus.global.common.dto.CustomPageRequest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryFinder {

	private final InquiryRepository inquiryRepository;

	private static final Integer PAGE_SIZE = 10;
	private static final String SORT_PROPERTY = "createdTime";

	public Slice<InquiryPreview> findAllUserInquiry(Long userId, int page) {

		// 1. Post 리스트를 Slice로 조회
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);

		Slice<Inquiry> inquiries = inquiryRepository.findAllByUserIdOrderByCreatedTimeDesc(userId,
			customPageRequest.of(SORT_PROPERTY));

		return inquiries.map(inquiry -> {
			InquiryDto inquiryDto = InquiryDto.from(inquiry);
			return InquiryPreview.from(inquiryDto);
		});
	}
}
