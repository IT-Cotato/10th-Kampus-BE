package com.cotato.kampus.domain.post.dto.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.dto.MyPostWithPhoto;

public record MyPostResponse(
	List<MyPostWithPhoto> posts,
	boolean hasNext
) {
	public static MyPostResponse from(Slice<MyPostWithPhoto> posts) {
		return new MyPostResponse(
			posts.getContent(),
			posts.hasNext()
		);
	}
}
