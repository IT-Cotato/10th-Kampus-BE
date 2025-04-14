package com.cotato.kampus.domain.chat.application;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.chat.implement.chatroom.ChatRoomAppender;
import com.cotato.kampus.domain.chat.implement.chatroom.ChatRoomValidator;
import com.cotato.kampus.domain.chat.implement.metadata.ChatroomMetadataAppender;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.post.domain.PostDto;
import com.cotato.kampus.domain.post.enums.PostStatus;
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
	ApiUserResolver apiUserResolver;
	@Mock
	ChatRoomValidator chatRoomValidator;
	@Mock
	ChatroomMetadataAppender chatroomMetadataAppender;

	@Test
	@DisplayName("postId로 채팅방을 생성한다.")
	public void createChatRoom() {
		//postId = 1, senderId = 1, receiverId = 2
		PostDto postDto = new PostDto(
			1L,
			2L,
			1L,
			"test",
			"test",
			1L,
			1L,
			1L,
			Anonymity.IDENTIFIED,
			PostStatus.PUBLISHED,
			2L,
			LocalDateTime.now()
		);

		when(postFinder.findPost(1L)).thenReturn(postDto);
		when(apiUserResolver.getCurrentUserId()).thenReturn(1L);
		doNothing().when(chatRoomValidator).validateDuplicateChatRoom(1L, 1L);
		when(chatRoomAppender.appendChatRoom(1L, 1L, 2L)).thenReturn(123L);
		doNothing().when(chatroomMetadataAppender).createMetadataPair(123L, postDto.id(),
			postDto.title(), 1L, 2L);

		Long id = target.createChatRoom(postDto.id());
		System.out.println("id = " + id);
		Assertions.assertThat(id).isEqualTo(123L);
	}

	@Test
	@DisplayName("postId에 해당하는 게시글이 없을 때 채팅방 생성에 실패한다.")
	public void createChatRoomWithNotExistingPost() {
		when(postFinder.findPost(1L)).thenThrow(new AppException(ErrorCode.POST_NOT_FOUND));
		Assertions.assertThatThrownBy(() -> target.createChatRoom(1L))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.POST_NOT_FOUND.getMessage());
	}
}