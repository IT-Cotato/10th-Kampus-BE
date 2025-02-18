package com.cotato.kampus.domain.chat.application;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.application.BoardFinder;
import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.cotato.kampus.domain.chat.domain.Chatroom;
import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;
import com.cotato.kampus.domain.chat.dto.ChatMessageSlice;
import com.cotato.kampus.domain.chat.dto.ChatMessageSliceSnapshot;
import com.cotato.kampus.domain.chat.dto.ChatNotification;
import com.cotato.kampus.domain.chat.dto.ChatNotificationResult;
import com.cotato.kampus.domain.chat.dto.ChatRoomDetailDto;
import com.cotato.kampus.domain.chat.dto.ChatRoomPreview;
import com.cotato.kampus.domain.chat.dto.ChatRoomPreviewList;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.application.PostFinder;
import com.cotato.kampus.domain.post.dto.PostDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class PostChatService {

	private final ChatRoomValidator chatRoomValidator;
	private final ChatRoomAppender chatRoomAppender;
	private final ChatRoomFinder chatRoomFinder;
	private final ChatRoomDeleter chatroomDeleter;

	private final ApiUserResolver apiUserResolver;
	private final ChatMemberFinder chatMemberFinder;

	private final PostFinder postFinder;
	private final BoardFinder boardFinder;

	private final ChatMessageFinder chatMessageFinder;
	private final ChatMessageAppender chatMessageAppender;
	private final ChatMessageDeleter chatMessageDeleter;
	private final ChatMessageProcessor chatMessageProcessor;
	private final MessageReadStatusUpdater messageReadStatusUpdater;
	private final MessageReadStatusDeleter messageReadStatusDeleter;

	private final ChatroomMetadataAppender chatroomMetadataAppender;
	private final ChatroomMetadataUpdater chatroomMetadataUpdater;
	private final ChatroomMetadataFinder chatroomMetadataFinder;
	private final ChatroomMetadataMapper chatroomMetadataMapper;
	private final ChatroomMetadataDeleter chatroomMetadataDeleter;

	@Transactional
	public Long createChatRoom(Long postId) {
		// 1. 게시글 정보 조회
		PostDto post = postFinder.findPost(postId);
		Long receiverId = post.userId();
		String postTitle = post.title();

		// 2. 채팅을 건 유저를 조회
		Long senderId = apiUserResolver.getCurrentUserId();

		// 3. 채팅방 생성 검증(보내는 사람 == 받는 사람 or 이미 있는 채팅방)
		chatRoomValidator.validateNewChatRoom(postId, senderId, receiverId);

		// 4. 채팅방 생성
		Long chatroomId = chatRoomAppender.appendChatRoom(postId, senderId, receiverId);

		// 5. 채팅방 리스트 조회시 사용되는 뷰 생성
		chatroomMetadataAppender.appendChatroomMetadatas(
			chatroomId,
			postId,
			postTitle,
			senderId,
			receiverId
		);

		return chatroomId;
	}

	public ChatRoomPreviewList findChatRooms(int page) {
		// 1. 유저 정보를 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 2. 해당 유저의 채팅방 메타데이터를 lastChatTime 내림차순으로 조회
		Slice<ChatroomMetadata> chatRoomMetadatas = chatroomMetadataFinder.findChatRoomMetadatas(userId, page);

		// 3. ChatRoomPreview로 변환
		List<ChatRoomPreview> previewList = chatRoomMetadatas.getContent()
			.stream()
			.map(chatroomMetadataMapper::toChatRoomPreview)
			.toList();

		return ChatRoomPreviewList.from(previewList, chatRoomMetadatas.hasNext());
	}

	public ChatRoomDetailDto getChatRoomDetail(Long chatroomId) {
		// 1. Find chatroom
		Chatroom chatroom = chatRoomFinder.findChatroom(chatroomId);

		// 2. Find associated post
		PostDto postDto = postFinder.findPost(chatroom.getPostId());

		// 3. Find board information
		BoardDto board = boardFinder.findBoard(postDto.boardId());

		return ChatRoomDetailDto.of(chatroom, postDto, board);
	}

	@Transactional
	public ChatNotificationResult processNewMessage(Long chatroomId, String message) {
		// 1. 메시지 보내는 유저의 id 조회
		Long senderId = apiUserResolver.getCurrentUserId();

		// 2. 메시지 저장
		ChatMessage chatMessage = chatMessageAppender.appendChatMessage(senderId, chatroomId, message);

		// 3. 메시지 수신자 id 조회
		Long receiverId = chatMemberFinder.findReceiverId(chatroomId, senderId);

		// 4. 발신자의 읽음 상태 업데이트 (메시지를 보낸 사람은 자동으로 읽음 처리)
		messageReadStatusUpdater.updateStatus(chatroomId, senderId, chatMessage.getId());

		// 5. 채팅방 메타데이터 업데이트
		chatroomMetadataUpdater.updateSenderMetadata(chatroomId, chatMessage, senderId);
		ChatroomMetadata receiverMetadata = chatroomMetadataUpdater.updateReceiverMetadata(chatroomId, chatMessage,
			receiverId);

		// 6. 수신자가 읽지 않은 메시지의 개수를 계산
		Long unreadCount = receiverMetadata.getUnreadCount();

		// 7. 알림 결과를 저장하여 리턴
		return ChatNotificationResult.of(chatMessage, ChatNotification.from(chatMessage, unreadCount), receiverId);
	}

	public ChatMessageSliceSnapshot getMessages(int page, Long chatroomId) {
		Long userId = apiUserResolver.getCurrentUserId();
		chatRoomValidator.validateUser(userId, chatroomId);

		ChatMessageSlice chatMessageSlice = chatMessageFinder.findAllByChatRoomId(page, chatroomId);
		// 조회 시점의 읽는 상태를 추가하여 반환
		return chatMessageProcessor.attachReadStatus(chatMessageSlice, chatroomId, userId);
	}

	@Transactional
	public void markMessagesAsRead(Long chatroomId) {
		Long userId = apiUserResolver.getCurrentUserId();
		// 1. 채팅방의 가장 최근 메시지 ID 조회
		ChatMessage latestMessage = chatMessageFinder.findLatestMessage(chatroomId);
		// 2. 해당 사용자의 메시지 읽음 상태 조회 또는 생성
		messageReadStatusUpdater.updateStatus(chatroomId, userId, latestMessage.getId());
		chatroomMetadataUpdater.resetReadCount(chatroomId, userId);
	}

	@Transactional
	public void deleteChatroom(Long chatroomId) {
		// 1. 현재 사용자 ID 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 2. 채팅방 멤버 검증
		chatRoomValidator.validateUser(userId, chatroomId);

		// 3. 채팅 메시지 삭제(sender, receiver)
		chatMessageDeleter.deleteByChatroomId(chatroomId);

		// 4. 읽음 상태 삭제(sender, receiver)
		messageReadStatusDeleter.deleteByChatroomId(chatroomId);

		// 5. 채팅방 메타데이터 삭제(sender, receiver)
		chatroomMetadataDeleter.deleteByChatroomId(chatroomId);

		// 6. 채팅방 삭제
		chatroomDeleter.deleteById(chatroomId);
	}
}