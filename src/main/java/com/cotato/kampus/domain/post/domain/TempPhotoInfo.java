package com.cotato.kampus.domain.post.domain;

public record TempPhotoInfo(
	int order,
	String photoUrl
) {
	public static TempPhotoInfo from(TemporaryPhoto temporaryPhoto) {
		return new TempPhotoInfo(
			temporaryPhoto.getOrder(),
			temporaryPhoto.getPhotoUrl()
		);
	}
}
