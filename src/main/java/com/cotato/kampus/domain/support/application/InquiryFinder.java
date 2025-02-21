package com.cotato.kampus.domain.support.application;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.support.dao.InquiryPhotoRepository;
import com.cotato.kampus.domain.support.dao.InquiryReplyRepository;
import com.cotato.kampus.domain.support.dao.InquiryRepository;
import com.cotato.kampus.domain.support.domain.Inquiry;
import com.cotato.kampus.domain.support.domain.InquiryPhoto;
import com.cotato.kampus.domain.support.domain.InquiryReply;
import com.cotato.kampus.domain.support.dto.InquiryDto;
import com.cotato.kampus.domain.support.dto.InquiryPreview;
import com.cotato.kampus.domain.support.dto.UserInquiryDetail;
import com.cotato.kampus.global.common.dto.CustomPageRequest;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryFinder {

	private final InquiryRepository inquiryRepository;

	private static final Integer PAGE_SIZE = 10;
	private static final String SORT_PROPERTY = "createdTime";
	private final InquiryPhotoRepository inquiryPhotoRepository;
	private final InquiryReplyRepository inquiryReplyRepository;

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

	public InquiryDto findInquiryDto(Long inquiryId) {
		Inquiry inquiry = inquiryRepository.findById(inquiryId)
			.orElseThrow(() -> new AppException(ErrorCode.INQUIRY_NOT_FOUND));

		return InquiryDto.from(inquiry);
	}

	public List<String> findInquiryPhotos(Long inquiryId) {
		return inquiryPhotoRepository.findAllByInquiryId(inquiryId)
			.stream()
			.map(InquiryPhoto::getPhotoUrl)
			.toList();
	}

	public InquiryReply findReplyByInquiryId(Long inquiryId) {
		return inquiryReplyRepository.findByInquiryId(inquiryId)
			.orElse(null);
	}
}
