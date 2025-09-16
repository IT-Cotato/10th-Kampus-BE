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
import com.cotato.kampus.global.error.exception.ChatRoomDuplicatedException;

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
	@DisplayName("postId로 채팅방을 생성하고 올바른 파라미터가 전달되는지 검증한다.")
	public void createChatRoom() {
		// given
		Long postId = 999L;
		Long senderId = 777L;
		Long receiverId = 555L;
		Long expectedChatRoomId = 123L;

		ChatReference chatReference = ChatReference.builder()
			.referenceId(postId)
			.referenceUserId(receiverId)
			.title("test")
			.build();

		when(referenceFinder.find(postId, ChatType.POST)).thenReturn(chatReference);
		when(apiUserResolver.getCurrentUserId()).thenReturn(senderId);
		doNothing().when(chatRoomValidator).validateDuplicateChatRoom(postId, senderId, ChatType.POST);
		when(chatRoomAppender.appendChatRoom(postId, ChatType.POST, senderId, receiverId)).thenReturn(expectedChatRoomId);
		doNothing().when(chatroomMetadataAppender)
			.createMetadataPair(expectedChatRoomId, ChatType.POST, postId,
				chatReference.getTitle(), senderId, receiverId);

		// when
		Long id = target.createChatRoom(postId, ChatType.POST);
		
		// then
		assertThat(id).isEqualTo(expectedChatRoomId);
		
		// 파라미터 순서 검증: 첫 번째는 postId여야 함 (receiverId가 아닌)
		verify(chatRoomAppender).appendChatRoom(
			eq(postId),      // referenceId = postId (NOT receiverId)
			eq(ChatType.POST),
			eq(senderId),
			eq(receiverId)
		);
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
		when(chatRoomAppender.appendChatRoom(1L, null, 1L, 2L)).thenReturn(123L);
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

	@Test
	@DisplayName("중복된 채팅방 생성 시 ChatRoomDuplicatedException이 발생하고 기존 채팅방 ID를 포함한다.")
	void createChatRoom_duplicated() {
		// given
		Long referenceId = 1L;
		Long senderId = 2L;
		Long existingChatRoomId = 999L;

		ChatReference chatReference = ChatReference.builder()
			.referenceId(referenceId)
			.referenceUserId(3L)
			.title("test")
			.build();

		when(referenceFinder.find(referenceId, ChatType.POST)).thenReturn(chatReference);
		when(apiUserResolver.getCurrentUserId()).thenReturn(senderId);
		doThrow(new ChatRoomDuplicatedException(ErrorCode.CHATROOM_DUPLICATED, existingChatRoomId))
			.when(chatRoomValidator).validateDuplicateChatRoom(referenceId, senderId, ChatType.POST);

		// when & then
		assertThatThrownBy(() -> target.createChatRoom(referenceId, ChatType.POST))
			.isInstanceOf(ChatRoomDuplicatedException.class)
			.hasMessage(ErrorCode.CHATROOM_DUPLICATED.getMessage())
			.extracting("existingChatRoomId")
			.isEqualTo(existingChatRoomId);

		// 중복 검증 이후 채팅방 생성 로직이 실행되지 않는지 확인
		verify(chatRoomAppender, never()).appendChatRoom(any(), any(), any(), any());
		verify(chatroomMetadataAppender, never()).createMetadataPair(any(), any(), any(), any(), any(), any());
	}

	@Test
	@DisplayName("중복된 PRODUCT 타입 채팅방 생성 시에도 ChatRoomDuplicatedException이 발생한다.")
	void createChatRoom_duplicated_product() {
		// given
		Long referenceId = 1L;
		Long senderId = 2L;
		Long existingChatRoomId = 888L;

		ChatReference chatReference = ChatReference.builder()
			.referenceId(referenceId)
			.referenceUserId(3L)
			.title("product test")
			.build();

		when(referenceFinder.find(referenceId, ChatType.PRODUCT)).thenReturn(chatReference);
		when(apiUserResolver.getCurrentUserId()).thenReturn(senderId);
		doThrow(new ChatRoomDuplicatedException(ErrorCode.CHATROOM_DUPLICATED, existingChatRoomId))
			.when(chatRoomValidator).validateDuplicateChatRoom(referenceId, senderId, ChatType.PRODUCT);

		// when & then
		assertThatThrownBy(() -> target.createChatRoom(referenceId, ChatType.PRODUCT))
			.isInstanceOf(ChatRoomDuplicatedException.class)
			.hasMessage(ErrorCode.CHATROOM_DUPLICATED.getMessage())
			.extracting("existingChatRoomId")
			.isEqualTo(existingChatRoomId);
	}

	@Test
	@DisplayName("중복 검증 통과 후 채팅방이 정상적으로 생성된다.")
	void createChatRoom_success_after_validation() {
		// given
		Long referenceId = 1L;
		Long senderId = 2L;
		Long receiverId = 3L;
		Long expectedChatRoomId = 100L;

		ChatReference chatReference = ChatReference.builder()
			.referenceId(referenceId)
			.referenceUserId(receiverId)
			.title("test chat")
			.build();

		when(referenceFinder.find(referenceId, ChatType.POST)).thenReturn(chatReference);
		when(apiUserResolver.getCurrentUserId()).thenReturn(senderId);
		doNothing().when(chatRoomValidator).validateDuplicateChatRoom(referenceId, senderId, ChatType.POST);
		when(chatRoomAppender.appendChatRoom(referenceId, ChatType.POST, senderId, receiverId))
			.thenReturn(expectedChatRoomId);
		doNothing().when(chatroomMetadataAppender)
			.createMetadataPair(expectedChatRoomId, ChatType.POST, referenceId,
				chatReference.getTitle(), senderId, receiverId);

		// when
		Long result = target.createChatRoom(referenceId, ChatType.POST);

		// then
		assertThat(result).isEqualTo(expectedChatRoomId);

		// 모든 단계가 순서대로 실행되는지 확인
		verify(referenceFinder).find(referenceId, ChatType.POST);
		verify(apiUserResolver).getCurrentUserId();
		verify(chatRoomValidator).validateDuplicateChatRoom(referenceId, senderId, ChatType.POST);
		verify(chatRoomAppender).appendChatRoom(referenceId, ChatType.POST, senderId, receiverId);
		verify(chatroomMetadataAppender).createMetadataPair(
			expectedChatRoomId, ChatType.POST, referenceId, chatReference.getTitle(), senderId, receiverId);
	}
}