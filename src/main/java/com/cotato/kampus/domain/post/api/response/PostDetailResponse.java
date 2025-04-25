package com.cotato.kampus.domain.post.api.response;

import java.time.LocalDateTime;
import java.util.List;

import com.cotato.kampus.domain.post.domain.PostDetails;
import com.cotato.kampus.domain.post.domain.PostPhotoInfo;
import com.fasterxml.jackson.annotation.JsonFormat;

public record PostDetailResponse(
	Long postId,
	String title,
	String content,
	int likes,
	int scraps,
	int comments,
	List<PostPhotoInfo> postPhotos,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime,
	boolean isAuthor,
	boolean isLiked,
	boolean isScrapped
) {
	public static PostDetailResponse from(PostDetails postDetails) {
		return new PostDetailResponse(
			postDetails.postId(),
			postDetails.title(),
			postDetails.content(),
			postDetails.likes(),
			postDetails.scraps(),
			postDetails.comments(),
			postDetails.postPhotos(),
			postDetails.createdTime(),
			postDetails.isAuthor(),
			postDetails.isLiked(),
			postDetails.isScrapped()
		);
	}
}