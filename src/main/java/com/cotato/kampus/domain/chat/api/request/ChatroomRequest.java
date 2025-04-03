package com.cotato.kampus.domain.chat.api.request;

import jakarta.validation.constraints.NotNull;

public record ChatroomRequest(
	@NotNull(message = "postId는 필수입니다.")
	Long postId
) {
}