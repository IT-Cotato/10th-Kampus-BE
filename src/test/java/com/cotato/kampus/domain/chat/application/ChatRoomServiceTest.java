package com.cotato.kampus.domain.chat.application;

import static org.mockito.Mockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.chat.domain.ChatReference;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.domain.chat.implement.ReferenceFinder;
import com.cotato.kampus.domain.chat.implement.chatroom.ChatRoomAppender;
import com.cotato.kampus.domain.chat.implement.chatroom.ChatRoomValidator;
import com.cotato.kampus.domain.chat.implement.metadata.ChatroomMetadataAppender;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

	@InjectMocks
	private ChatRoomServiceImpl target;

	@Mock
	private ChatRoomAppender chatRoomAppender;
	@Mock
	private PostFinder postFinder;
	@Mock
	private ReferenceFinder referenceFinder;
	@Mock
	ApiUserResolver apiUserResolver;
	@Mock
	ChatRoomValidator chatRoomValidator;
	@Mock
	ChatroomMetadataAppender chatroomMetadataAppender;

	@Test
	@DisplayName("postId로 채팅방을 생성한다.")
	public void createChatRoom() {
		//postId = 1, senderId = 1, receiverId = 2

		ChatReference chatReference = ChatReference.builder()
			.referenceId(1L)
			.referenceUserId(2L)
			.title("test")
			.build();

		when(referenceFinder.find(1L, ChatType.POST)).thenReturn(chatReference);
		when(apiUserResolver.getCurrentUserId()).thenReturn(1L);
		doNothing().when(chatRoomValidator).validateDuplicateChatRoom(1L, 1L, ChatType.POST);
		when(chatRoomAppender.appendChatRoom(2L, ChatType.POST, 1L, 2L)).thenReturn(123L);
		doNothing().when(chatroomMetadataAppender)
			.createMetadataPair(123L, ChatType.POST, chatReference.getReferenceId(),
				chatReference.getTitle(), 1L, 2L);

		Long id = target.createChatRoom(chatReference.getReferenceId(), ChatType.POST);
		Assertions.assertThat(id).isEqualTo(123L);
	}

	@Test
	@DisplayName("postId에 해당하는 게시글이 없을 때 채팅방 생성에 실패한다.")
	public void createChatRoomWithNotExistingPost() {
		when(referenceFinder.find(1L, ChatType.POST)).thenThrow(new AppException(ErrorCode.POST_NOT_FOUND));
		Assertions.assertThatThrownBy(() -> target.createChatRoom(1L, ChatType.POST))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.POST_NOT_FOUND.getMessage());
	}
}