package com.cotato.kampus.domain.chat.dto.response;

public record ChatroomResponse(
	Long chatRoomId
) {

	public static ChatroomResponse of(Long chatRoomId) {
		return new ChatroomResponse(
			chatRoomId
		);
	}
}