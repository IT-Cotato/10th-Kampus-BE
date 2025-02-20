package com.cotato.kampus.domain.notice.dto;

import java.util.List;

public record NoticeList(
	List<NoticeDto> notices
) {
	public static NoticeList of(List<NoticeDto> notices) {
		return new NoticeList(notices);
	}
}
