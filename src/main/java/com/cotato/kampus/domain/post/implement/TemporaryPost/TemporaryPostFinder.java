package com.cotato.kampus.domain.post.implement.TemporaryPost;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.domain.TemporaryPost;
import com.cotato.kampus.domain.post.implement.port.TemporaryPostRepository;
import com.cotato.kampus.global.common.dto.CustomPageRequest;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TemporaryPostFinder {

	private static final int PAGE_SIZE = 10;
	private static final String SORT_PROPERTY = "createdTime";
	private final TemporaryPostRepository temporaryPostRepository;


	public TemporaryPost find(Long temporaryPostId) {
		return temporaryPostRepository.findById(temporaryPostId)
			.orElseThrow(() -> new AppException(ErrorCode.TEMP_POST_NOT_FOUND));
	}

	public List<TemporaryPost> findAllByIds(List<Long> temporaryPostIds) {
		return temporaryPostRepository.findAllByIdIn(temporaryPostIds);
	}

	public List<TemporaryPost> findAllByUserId(Long userId) {
		return temporaryPostRepository.findAllByUserId(userId);
	}

	public Slice<TemporaryPost> findAllByUserId(Long userId, int page) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		return temporaryPostRepository.findAllByUserId(userId, customPageRequest.of(SORT_PROPERTY));
	}

}
