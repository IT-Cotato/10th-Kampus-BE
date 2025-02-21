package com.cotato.kampus.domain.support.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.support.dto.InquiryDto;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryValidator {

	public void validateUserIsAuthor(Long userId, InquiryDto inquiryDto) {
		if(!inquiryDto.userId().equals(userId))
			throw new AppException(ErrorCode.INQUIRY_NOT_AUTHOR);
	}
}
