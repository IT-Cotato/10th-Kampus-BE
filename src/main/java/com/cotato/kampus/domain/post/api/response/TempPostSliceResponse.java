package com.cotato.kampus.domain.post.api.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.domain.TempPostThumbnail;

public record TempPostSliceResponse<T>(
	List<TempPostThumbnail> tempPosts,
	Boolean hasNext,
	int totalCount
) {
	public static <T> TempPostSliceResponse<T> from (Slice<TempPostThumbnail> tempPosts, int totalCount) {
		return new TempPostSliceResponse<>(
			tempPosts.getContent(),
			tempPosts.hasContent(),
			totalCount
		);
	}
}
