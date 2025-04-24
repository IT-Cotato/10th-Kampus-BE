package com.cotato.kampus.domain.chat.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.chat.api.port.ChatMessageService;
import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.cotato.kampus.domain.chat.domain.ChatMessageSlice;
import com.cotato.kampus.domain.chat.domain.ChatMessageSliceSnapshot;
import com.cotato.kampus.domain.chat.domain.ChatNotification;
import com.cotato.kampus.domain.chat.domain.ChatNotificationResult;
import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;
import com.cotato.kampus.domain.chat.implement.ChatMemberFinder;
import com.cotato.kampus.domain.chat.implement.chatroom.ChatRoomValidator;
import com.cotato.kampus.domain.chat.implement.message.ChatMessageAppender;
import com.cotato.kampus.domain.chat.implement.message.ChatMessageFinder;
import com.cotato.kampus.domain.chat.implement.message.ChatMessageProcessor;
import com.cotato.kampus.domain.chat.implement.metadata.ChatroomMetadataUpdater;
import com.cotato.kampus.domain.chat.implement.read.MessageReadStatusUpdater;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.util.s3.S3Uploader;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {
	private final ChatRoomValidator chatRoomValidator;

	private final ApiUserResolver apiUserResolver;
	private final ChatMemberFinder chatMemberFinder;

	private final ChatMessageFinder chatMessageFinder;
	private final ChatMessageAppender chatMessageAppender;
	private final ChatMessageProcessor chatMessageProcessor;
	private final MessageReadStatusUpdater messageReadStatusUpdater;

	private final ChatroomMetadataUpdater chatroomMetadataUpdater;
	private final S3Uploader s3Uploader;

	private static final String IMAGE_FOLDER = "chat";

	@Override
	@Transactional
	public ChatNotificationResult processNewMessage(Long chatroomId, boolean isImage, String message) {
		// 1. 메시지 보내는 유저의 id 조회
		Long senderId = apiUserResolver.getCurrentUserId();

		// 2. 메시지 저장
		ChatMessage chatMessage = chatMessageAppender.appendChatMessage(senderId, chatroomId, isImage, message);

		// 3. 메시지 수신자 id 조회
		Long receiverId = chatMemberFinder.findReceiverId(chatroomId, senderId);

		// 4. 발신자의 읽음 상태 업데이트 (메시지를 보낸 사람은 자동으로 읽음 처리)
		messageReadStatusUpdater.updateStatus(chatroomId, senderId, chatMessage.getId());

		// 5. 채팅방 메타데이터 업데이트
		chatroomMetadataUpdater.updateSenderMetadata(chatroomId, chatMessage, senderId);
		ChatroomMetadata receiverMetadata = chatroomMetadataUpdater.updateReceiverMetadata(chatroomId,
			chatMessage,
			receiverId);

		// 6. 수신자가 읽지 않은 메시지의 개수를 계산
		Long unreadCount = receiverMetadata.getUnreadCount();

		// 7. 알림 결과를 저장하여 리턴
		return ChatNotificationResult.of(chatMessage, ChatNotification.from(chatMessage, unreadCount),
			receiverId);
	}

	@Override
	public ChatMessageSliceSnapshot getMessages(int page, Long chatroomId) {
		Long userId = apiUserResolver.getCurrentUserId();
		chatRoomValidator.validateEnteredUser(userId, chatroomId);

		ChatMessageSlice chatMessageSlice = chatMessageFinder.findAllByChatRoomId(page, chatroomId);
		// 조회 시점의 읽는 상태를 추가하여 반환
		return chatMessageProcessor.attachReadStatus(chatMessageSlice, chatroomId, userId);
	}

	@Override
	@Transactional
	public void markMessagesAsRead(Long chatroomId) {
		Long userId = apiUserResolver.getCurrentUserId();
		// 1. 채팅방의 가장 최근 메시지 ID 조회
		ChatMessage latestMessage = chatMessageFinder.findLatestMessage(chatroomId);
		// 2. 해당 사용자의 메시지 읽음 상태 조회 또는 생성
		messageReadStatusUpdater.updateStatus(chatroomId, userId, latestMessage.getId());
		chatroomMetadataUpdater.resetReadCount(chatroomId, userId);
	}

	@Override
	@Transactional
	public List<String> uploadImage(Long chatroomId, List<MultipartFile> images) throws ImageException {
		Long userId = apiUserResolver.getCurrentUserId();
		chatRoomValidator.validateUser(userId, chatroomId);
		// 1. 이미지 S3 업로드
		return s3Uploader.uploadFiles(images, IMAGE_FOLDER);
	}
}
