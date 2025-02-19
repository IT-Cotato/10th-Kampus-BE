package com.cotato.kampus.domain.post.dto.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.admin.dto.response.AdminCardNewsPreview;
import com.cotato.kampus.domain.post.dto.PostWithPhotos;

public record AdminCardNewsListResponse(
	List<AdminCardNewsPreview> posts,
	Boolean hasNext
) {
	public static AdminCardNewsListResponse from(Slice<AdminCardNewsPreview> posts) {
		return new AdminCardNewsListResponse(
			posts.getContent(),
			posts.hasNext()
		);
	}
}
