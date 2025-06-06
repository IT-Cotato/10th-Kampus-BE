package com.cotato.kampus.domain.chat.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.cotato.kampus.domain.chat.domain.ChatReference;
import com.cotato.kampus.domain.chat.domain.ChatRoomPreview;
import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.domain.chat.implement.ReferenceFinder;
import com.cotato.kampus.domain.chat.implement.chatroom.ChatRoomAppender;
import com.cotato.kampus.domain.chat.implement.chatroom.ChatRoomValidator;
import com.cotato.kampus.domain.chat.implement.metadata.ChatroomMetadataAppender;
import com.cotato.kampus.domain.chat.implement.metadata.ChatroomMetadataFinder;
import com.cotato.kampus.domain.chat.implement.metadata.ChatroomMetadataMapper;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

	@InjectMocks
	private ChatRoomServiceImpl target;

	@Mock
	private ChatRoomAppender chatRoomAppender;
	@Mock
	private ReferenceFinder referenceFinder;
	@Mock
	ApiUserResolver apiUserResolver;
	@Mock
	ChatRoomValidator chatRoomValidator;
	@Mock
	ChatroomMetadataAppender chatroomMetadataAppender;
	@Mock
	ChatroomMetadataFinder chatroomMetadataFinder;
	@Mock
	ChatroomMetadataMapper chatroomMetadataMapper;

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
		assertThat(id).isEqualTo(123L);
	}

	@Test
	@DisplayName("postId에 해당하는 게시글이 없을 때 채팅방 생성에 실패한다.")
	public void createChatRoomWithNotExistingPost() {
		when(referenceFinder.find(1L, ChatType.POST)).thenThrow(new AppException(ErrorCode.POST_NOT_FOUND));
		Assertions.assertThatThrownBy(() -> target.createChatRoom(1L, ChatType.POST))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.POST_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("채팅방 type = ALL로 생성시 문제 생김")
	public void createChatRoomWithAllType() {
		ChatReference chatReference = ChatReference.builder()
			.referenceId(1L)
			.referenceUserId(2L)
			.title("test")
			.build();

		when(referenceFinder.find(1L, null)).thenReturn(chatReference);
		when(apiUserResolver.getCurrentUserId()).thenReturn(1L);
		doNothing().when(chatRoomValidator).validateDuplicateChatRoom(1L, 1L, null);
		when(chatRoomAppender.appendChatRoom(2L, null, 1L, 2L)).thenReturn(123L);
		doNothing().when(chatroomMetadataAppender)
			.createMetadataPair(123L, null, chatReference.getReferenceId(),
				chatReference.getTitle(), 1L, 2L);

		Long id = target.createChatRoom(chatReference.getReferenceId(), null);
		assertThat(id).isEqualTo(123L);
	}

	@ParameterizedTest
	@EnumSource(value = ChatType.class, names = {"POST", "PRODUCT"})
	@DisplayName("ChatType별로 해당 타입의 채팅방만 반환한다.")
	void findChatRooms_byType(ChatType type) {
		when(apiUserResolver.getCurrentUserId()).thenReturn(1L);
		List<ChatroomMetadata> metaList = List.of(ChatroomMetadataFactory.create(type));
		Slice<ChatroomMetadata> metaSlice = new SliceImpl<>(metaList);
		when(chatroomMetadataFinder.findChatRoomMetadatas(1L, 1, type)).thenReturn(metaSlice);
		var preview = mock(ChatRoomPreview.class);
		when(chatroomMetadataMapper.toChatRoomPreview(any())).thenReturn(preview);

		var result = target.findChatRooms(1, type);
		assertThat(result.chatRoomPreviewList()).containsExactly(preview);
	}

	@ParameterizedTest
	@MethodSource("allTypeProvider")
	@DisplayName("ChatType.ALL이면 모든 타입의 채팅방을 반환한다.")
	void findChatRooms_allType(List<ChatType> types) {
		when(apiUserResolver.getCurrentUserId()).thenReturn(1L);
		List<ChatroomMetadata> metaList = ChatroomMetadataFactory.createList(types.toArray(new ChatType[0]));
		Slice<ChatroomMetadata> metaSlice = new SliceImpl<>(metaList);
		when(chatroomMetadataFinder.findChatRoomMetadatas(1L, 1, null)).thenReturn(metaSlice);
		var preview = mock(ChatRoomPreview.class);
		when(chatroomMetadataMapper.toChatRoomPreview(any())).thenReturn(preview);

		var result = target.findChatRooms(1, null);
		assertThat(result.chatRoomPreviewList()).hasSize(types.size());
	}

	static Stream<List<ChatType>> allTypeProvider() {
		return Stream.of(
			List.of(ChatType.POST, ChatType.PRODUCT),
			List.of(ChatType.POST),
			List.of(ChatType.PRODUCT)
		);
	}

	@Test
	@DisplayName("조회 결과가 없으면 빈 리스트를 반환한다.")
	void findChatRooms_empty() {
		when(apiUserResolver.getCurrentUserId()).thenReturn(1L);
		Slice<ChatroomMetadata> metaSlice = new SliceImpl<>(List.of());
		when(chatroomMetadataFinder.findChatRoomMetadatas(1L, 1, null)).thenReturn(metaSlice);

		var result = target.findChatRooms(1, null);
		assertThat(result.chatRoomPreviewList()).isEmpty();
	}
}