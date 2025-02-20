package com.cotato.kampus.domain.notice.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.notice.dao.NoticeRepository;
import com.cotato.kampus.domain.notice.domain.Notice;
import com.cotato.kampus.domain.user.application.UserValidator;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class NoticeService {

	private final NoticeRepository noticeRepository;
	private final UserValidator userValidator;

	public Long createNotice(String title, String content) {
		userValidator.validateAdminAccess();
		return noticeRepository.save(
			Notice.builder()
				.title(title)
				.content(content)
				.build()
		).getId();
	}
}