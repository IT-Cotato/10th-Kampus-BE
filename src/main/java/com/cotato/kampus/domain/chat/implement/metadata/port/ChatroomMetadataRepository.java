package com.cotato.kampus.domain.chat.implement.metadata.port;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;
import com.cotato.kampus.domain.chat.enums.ChatType;

public interface ChatroomMetadataRepository {

	void save(ChatroomMetadata senderMetadata);

	void saveAll(List<ChatroomMetadata> chatroomMetadata);

	Slice<ChatroomMetadata> findAllByUserIdAndChatTypeOrderByLastChatTimeDesc(Long userId, ChatType chatType,
		Pageable pageable);

	Optional<ChatroomMetadata> findByChatroomIdAndUserId(Long chatroomId, Long senderId);

	void deleteAllByChatroomId(Long chatroomId);

}
