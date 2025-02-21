package com.cotato.kampus.domain.support.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.support.domain.InquiryReply;
import com.cotato.kampus.domain.support.dto.InquiryDto;
import com.cotato.kampus.domain.support.dto.UserInquiryDetail;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryMapper {

	private final InquiryFinder inquiryFinder;

	public UserInquiryDetail mapToInquiryDetail(InquiryDto inquiryDto) {

		List<String> photos = inquiryFinder.findInquiryPhotos(inquiryDto.inquiryId());

		InquiryReply inquiryReply = inquiryFinder.findReplyByInquiryId(inquiryDto.inquiryId());

		return UserInquiryDetail.from(inquiryDto, photos, inquiryReply);
	}
}
