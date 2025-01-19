package com.cotato.kampus.domain.post.dto;

public record AnonymousOrPostAuthor(
	Boolean isAnonymous,
	Boolean isAuthor,
	Long userId,
	String username,
	String profileImage
) {
	public static AnonymousOrPostAuthor of(Boolean isAnonymous, Boolean isAuthor, Long userId, String username,
		String profileImage) {
		return new AnonymousOrPostAuthor(
			isAnonymous,
			isAuthor,
			userId,
			username,
			profileImage
		);
	}
}