package com.cotato.kampus.domain.post.domain;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TemporaryPhoto {

	private final Long id;
	private final Long temporaryPostId;
	private final String photoUrl;

	@Builder
	public TemporaryPhoto(Long id, Long temporaryPostId, String photoUrl) {
		this.id = id;
		this.temporaryPostId = temporaryPostId;
		this.photoUrl = photoUrl;
		validate();
	}

	private void validate() {
		if (temporaryPostId == null) {
			throw new AppException(ErrorCode.TEMP_POST_ID_REQUIRED);
		}
		if (photoUrl == null) {
			throw new AppException(ErrorCode.TEMP_PHOTO_URL_REQUIRED);
		}
	}
}
