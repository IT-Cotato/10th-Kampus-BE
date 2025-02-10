package com.cotato.kampus.domain.chat.application;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.application.BoardFinder;
import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.cotato.kampus.domain.chat.domain.Chatroom;
import com.cotato.kampus.domain.chat.dto.ChatMessageSlice;
import com.cotato.kampus.domain.chat.dto.ChatMessageSliceWithUserId;
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
	private final ChatRoomMapper chatRoomMapper;

	private final ApiUserResolver apiUserResolver;
	private final ChatMemberFinder chatMemberFinder;

	private final PostFinder postFinder;
	private final BoardFinder boardFinder;

	private final ChatMessageFinder chatMessageFinder;
	private final ChatMessageAppender chatMessageAppender;
	private final ChatMessageCounter chatMessageCounter;
	private final MessageReadStatusUpdater messageReadStatusUpdater;

	@Transactional
	public Long createChatRoom(Long postId) {
		// 1. 게시글 작성자를 조회
		Long receiverId = postFinder.findPost(postId).userId();

		// 2. 채팅을 건 유저를 조회
		Long senderId = apiUserResolver.getUserId();

		// 3. 채팅방 생성 검증(보내는 사람 == 받는 사람 or 이미 있는 채팅방)
		chatRoomValidator.validateNewChatRoom(postId, senderId, receiverId);

		// 4. 채팅방 생성
		return chatRoomAppender.appendChatRoom(postId, senderId, receiverId);
	}

	public ChatRoomPreviewList findChatRooms(int page) {
		// 1. 유저 정보를 조회
		Long userId = apiUserResolver.getUserId();

		// 2. user가 속한 채팅방을 조회
		Slice<Chatroom> chatRooms = chatRoomFinder.findChatRooms(userId, page);

		// 3. 채팅방의 마지막 메시지와 채팅 시간 필드, 읽지 않은 메시지 수를 추가하여 매핑
		List<ChatRoomPreview> previewList = chatRooms.getContent()
			.stream()
			.map((chatroom) -> chatRoomMapper.toChatRoomPreview(chatroom, userId))
			.toList();

		return ChatRoomPreviewList.from(previewList, chatRooms.hasNext());
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
		Long senderId = apiUserResolver.getUserId();

		// 2. 메시지 저장
		ChatMessage chatMessage = chatMessageAppender.appendChatMessage(senderId, chatroomId, message);

		// 3. 메시지 수신자 id 조회
		Long receiverId = chatMemberFinder.findReceiverId(chatroomId, senderId);

		// 4. 수신자가 읽지 않은 메시지의 개수를 계산
		Long unreadCount = chatMessageCounter.countUnreadMessages(chatroomId, receiverId);

		// 5. 알림 결과를 저장하여 리턴
		return ChatNotificationResult.of(chatMessage, ChatNotification.from(chatMessage, unreadCount), receiverId);
	}

	public ChatMessageSliceWithUserId getMessages(int page, Long chatroomId) {
		// 1. 조회한 유저 id 조회
		Long userId = apiUserResolver.getUserId();

		// 2. 조회한 유저가 채팅방에 있는 유저인지 조회
		chatRoomValidator.validateUser(userId, chatroomId);

		// 3. Slice로 채팅 메시지 조회하여 현재 유저 id와 함께 반환
		ChatMessageSlice chatMessageSlice = chatMessageFinder.findAllByChatRoomId(page, chatroomId);
		return ChatMessageSliceWithUserId.from(userId, chatMessageSlice);
	}

	@Transactional
	public void markMessagesAsRead(Long chatroomId) {
		Long userId = apiUserResolver.getUserId();
		// 1. 채팅방의 가장 최근 메시지 ID 조회
		Long latestMessageId = chatMessageFinder.findLatestMessageId(chatroomId);
		// 2. 해당 사용자의 메시지 읽음 상태 조회 또는 생성
		messageReadStatusUpdater.updateStatus(chatroomId, userId, latestMessageId);
	}
}