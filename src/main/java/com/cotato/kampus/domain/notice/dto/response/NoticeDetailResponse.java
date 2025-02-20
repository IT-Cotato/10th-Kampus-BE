package com.cotato.kampus.domain.notice.dto.response;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.notice.dto.NoticeDto;
import com.fasterxml.jackson.annotation.JsonFormat;

public record NoticeDetailResponse(
	Long id,
	String title,
	String content,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime
) {
	public static NoticeDetailResponse from(NoticeDto notice) {
		return new NoticeDetailResponse(
			notice.noticeId(),
			notice.title(),
			notice.content(),
			notice.createdTime()
		);
	}
}
