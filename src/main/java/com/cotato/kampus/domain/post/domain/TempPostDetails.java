package com.cotato.kampus.domain.post.domain;

import java.util.List;

public record TempPostDetails(
	Long tempPostId,
	Long boardId,
	String boardName,
	String title,
	String content,
	List<TempPhotoInfo> postPhotos
) {
	public static TempPostDetails of(TemporaryPost tempPost, String boardName, List<TemporaryPhoto> postPhotos) {
		return new TempPostDetails(
			tempPost.getId(),
			tempPost.getBoardId(),
			boardName,
			tempPost.getTitle(),
			tempPost.getContent(),
			postPhotos.stream()
				.map(TempPhotoInfo::from)
				.toList()
		);
	}
}
