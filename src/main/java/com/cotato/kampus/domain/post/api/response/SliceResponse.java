package com.cotato.kampus.domain.post.api.response;

import java.util.List;

import org.springframework.data.domain.Slice;

public record SliceResponse<T>(
	List<T> items,
	Boolean hasNext
) {
	public static <T> SliceResponse<T> from(Slice<T> slice) {
		return new SliceResponse<>(
			slice.getContent(),
			slice.hasContent()
		);
	}
}
