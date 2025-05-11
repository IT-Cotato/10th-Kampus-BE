package com.cotato.kampus.domain.chat.implement.chatroom.port;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.chat.domain.ChatRoom;
import com.cotato.kampus.domain.chat.enums.ChatType;

public interface ChatRoomRepository {

	Long save(ChatRoom chatRoom);

	boolean existsByReferenceIdAndInitialSenderIdAndChatType(Long referenceId, Long senderId, ChatType chatType);

	Slice<ChatRoom> findAllByUserIdOrderByCreatedTimeDesc(Long userId, Pageable pageable);

	Optional<ChatRoom> findById(Long chatroomId);
}