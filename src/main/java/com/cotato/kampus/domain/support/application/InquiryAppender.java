package com.cotato.kampus.domain.support.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.support.dao.InquiryRepository;
import com.cotato.kampus.domain.support.domain.Inquiry;
import com.cotato.kampus.domain.support.enums.InquiryStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryAppender {

	private final InquiryRepository inquiryRepository;

	@Transactional
	public Long append(Long userId, String title, String content) {

		Inquiry inquiry = Inquiry.builder()
			.userId(userId)
			.title(title)
			.content(content)
			.inquiryStatus(InquiryStatus.PENDING)
			.build();

		return inquiryRepository.save(inquiry).getId();
	}
}
