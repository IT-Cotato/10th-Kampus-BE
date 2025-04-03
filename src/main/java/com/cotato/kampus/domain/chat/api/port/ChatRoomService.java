package com.cotato.kampus.domain.chat.api.port;

import com.cotato.kampus.domain.chat.domain.ChatRoomDetailDto;
import com.cotato.kampus.domain.chat.domain.ChatRoomPreviewList;

public interface ChatRoomService {

	Long createChatRoom(Long postId);

	ChatRoomPreviewList findChatRooms(int page);

	ChatRoomDetailDto getChatRoomDetail(Long chatroomId);

	void deleteChatroom(Long chatRoomId);
}