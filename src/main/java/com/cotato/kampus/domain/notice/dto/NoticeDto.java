package com.cotato.kampus.domain.notice.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.notice.domain.Notice;

public record NoticeDto(
	Long noticeId,
	String title,
	String content,
	LocalDateTime createdTime
) {
	public static NoticeDto from(Notice notice) {
		return new NoticeDto(
			notice.getId(),
			notice.getTitle(),
			notice.getContent(),
			notice.getCreatedTime()
		);
	}
}
