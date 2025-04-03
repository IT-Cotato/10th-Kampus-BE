package com.cotato.kampus.domain.chat.api.port;

import com.cotato.kampus.domain.chat.domain.ChatRoomDetailDto;
import com.cotato.kampus.domain.chat.domain.ChatRoomPreview;

public interface ChatRoomService {

	public Long createChatRoom(Long id);

	public ChatRoomPreview findChatRooms(int page);

	public ChatRoomDetailDto getChatRoomDetail(Long chatroomId);

	public void deleteChatroom(Long chatRoomId);
}