package com.cotato.kampus.domain.chat.api.port;

import com.cotato.kampus.domain.chat.domain.ChatRoomDetailDto;
import com.cotato.kampus.domain.chat.domain.ChatRoomPreviewList;

public interface ChatRoomService {

	public Long createChatRoom(Long postId);

	public ChatRoomPreviewList findChatRooms(int page);

	public ChatRoomDetailDto getChatRoomDetail(Long chatroomId);

	public void deleteChatroom(Long chatRoomId);
}