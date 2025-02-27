package com.cotato.kampus.domain.chat.application;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.application.PostFinder;
import com.cotato.kampus.domain.post.dto.PostDto;
import com.cotato.kampus.domain.post.enums.PostCategory;
import com.cotato.kampus.domain.post.enums.PostStatus;

@ExtendWith(MockitoExtension.class)
class PostChatServiceTest {

	@InjectMocks
	private PostChatService target;

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
	//postId = 1, senderId = 1, receiverId = 2
	public void 채팅방_생성() {
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
			PostCategory.HOSPITAL,
			2L,
			LocalDateTime.now()
		);

		when(postFinder.findPost(1L)).thenReturn(postDto);
		when(apiUserResolver.getCurrentUserId()).thenReturn(1L);
		doNothing().when(chatRoomValidator).validateNewChatRoom(1L, 1L, 2L);
		when(chatRoomAppender.appendChatRoom(1L, 1L, 2L)).thenReturn(123L);
		doNothing().when(chatroomMetadataAppender).appendChatroomMetadatas(123L, postDto.id(),
			postDto.title(), 1L, 2L);

		Long id = target.createChatRoom(postDto.id());
		System.out.println("id = " + id);
		Assertions.assertThat(id).isEqualTo(123L);
	}
}