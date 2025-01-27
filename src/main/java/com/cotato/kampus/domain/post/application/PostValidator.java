package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostValidator {

	public void validatePublishable(
		Long boardId,
		String title,
		String content,
		Anonymity anonymity
	){
		if(boardId == null || title == null || content == null || anonymity == null){
			throw new AppException(ErrorCode.POST_PUBLISH_INVALID);
		}
	}
}
