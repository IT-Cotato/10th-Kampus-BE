package com.cotato.kampus.domain.post.implement.post;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.PostScrapRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostValidator {

	private final PostScrapRepository postScrapRepository;

	public void validateDuplicatedScrap(Long postId, Long userId) {
		// 중복 스크랩 예외처리
		if (postScrapRepository.existsByUserIdAndPostId(userId, postId)) {
			throw new AppException(ErrorCode.POST_SCRAP_DUPLICATED);
		}
	}
}
