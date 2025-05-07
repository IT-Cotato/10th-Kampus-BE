package com.cotato.kampus.domain.chat.api.port;

import com.cotato.kampus.domain.chat.domain.ChatRoomDetailDto;
import com.cotato.kampus.domain.chat.domain.ChatRoomPreviewList;
import com.cotato.kampus.domain.chat.enums.ChatType;

public interface ChatRoomService {

	Long createChatRoom(Long referenceId, ChatType chatType);

	ChatRoomPreviewList findChatRooms(int page);

	ChatRoomDetailDto getChatRoomDetail(Long chatroomId);

	void deleteChatroom(Long chatRoomId);
}