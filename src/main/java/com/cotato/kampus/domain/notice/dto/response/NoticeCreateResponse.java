package com.cotato.kampus.domain.notice.dto.response;

public record NoticeCreateResponse(
	Long noticeId
) {
	public static NoticeCreateResponse of(Long noticeId) {
		return new NoticeCreateResponse(noticeId);
	}
}