package com.cotato.kampus.domain.post.domain;

public record PostPhotoInfo(
	int order,
	String photoUrl
) {
	public static PostPhotoInfo from(PostPhoto postPhoto) {
		return new PostPhotoInfo(
			postPhoto.getOrder(),
			postPhoto.getPhotoUrl()
		);
	}
}
