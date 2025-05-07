package com.cotato.kampus.domain.chat.implement.metadata;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.domain.chat.implement.metadata.port.ChatroomMetadataRepository;

@ExtendWith(MockitoExtension.class)
class ChatroomMetadataAppenderTest {

	@InjectMocks
	private ChatroomMetadataAppender chatroomMetadataAppender;

	@Mock
	private ChatroomMetadataRepository chatroomMetadataRepository;

	@Test
	@DisplayName("채팅방 메타데이터 쌍 생성 성공")
	public void createMetadataPair() {
		// given
		Long chatroomId = 1L;
		Long referenceId = 2L;
		ChatType chatType = ChatType.POST;
		String title = "Test Post";
		Long senderId = 3L;
		Long receiverId = 4L;

		doNothing().when(chatroomMetadataRepository).saveAll(anyList());

		// when
		chatroomMetadataAppender.createMetadataPair(
			chatroomId,
			chatType,
			referenceId,
			title,
			senderId,
			receiverId
		);

		// then
		verify(chatroomMetadataRepository).saveAll(anyList());
	}

}