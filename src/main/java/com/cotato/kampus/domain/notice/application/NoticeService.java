package com.cotato.kampus.domain.notice.application;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.notice.dao.NoticeRepository;
import com.cotato.kampus.domain.notice.domain.Notice;
import com.cotato.kampus.domain.notice.dto.NoticeDto;
import com.cotato.kampus.domain.notice.dto.NoticeList;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.global.common.dto.CustomPageRequest;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeService {

	private final NoticeRepository noticeRepository;
	private final UserValidator userValidator;

	private static final Integer PAGE_SIZE = 10;
	private static final String SORT_PROPERTY = "createdTime";

	@Transactional
	public Long createNotice(String title, String content) {
		userValidator.validateAdminAccess();
		return noticeRepository.save(
			Notice.builder()
				.title(title)
				.content(content)
				.build()
		).getId();
	}

	public NoticeList getNotices(int page) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		return NoticeList.of(
			noticeRepository.findAll(customPageRequest.of(SORT_PROPERTY))
				.stream()
				.map(NoticeDto::from)
				.toList()
		);
	}

	public NoticeDto getNotice(Long noticeId) {
		return NoticeDto.from(
			noticeRepository.findById(noticeId)
				.orElseThrow(() -> new AppException(ErrorCode.NOTICE_NOT_FOUND))
		);
	}

	@Transactional
	public void deleteNotice(Long noticeId) {
		userValidator.validateAdminAccess();
		noticeRepository.deleteById(noticeId);
	}

	@Transactional
	public void updateNotice(Long noticeId, String title, String content) {
		userValidator.validateAdminAccess();
		Notice notice = noticeRepository.findById(noticeId)
			.orElseThrow(() -> new AppException(ErrorCode.NOTICE_NOT_FOUND));
		notice.update(title, content);
	}

}