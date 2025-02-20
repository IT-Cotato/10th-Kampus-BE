package com.cotato.kampus.domain.notice.dto.response;

import java.util.List;

import com.cotato.kampus.domain.notice.dto.NoticeList;

public record NoticeListResponse(
	List<NoticeDetailResponse> notices
) {
	public static NoticeListResponse from(NoticeList noticeList) {
		return new NoticeListResponse(
			noticeList.notices()
				.stream()
				.map(NoticeDetailResponse::from)
				.toList()
		);
	}

}
