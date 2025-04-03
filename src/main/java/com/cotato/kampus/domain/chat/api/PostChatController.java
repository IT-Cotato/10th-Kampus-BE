package com.cotato.kampus.domain.chat.api;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cotato.kampus.domain.chat.api.request.ChatMessageRequest;
import com.cotato.kampus.domain.chat.api.response.ChatMessageListResponse;
import com.cotato.kampus.domain.chat.application.PostChatService;
import com.cotato.kampus.domain.chat.domain.ChatMessageSliceSnapshot;
import com.cotato.kampus.domain.chat.domain.ChatNotificationResult;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시글 채팅 API", description = "게시글 채팅 API")
@Controller
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/chats")
public class PostChatController {

	private final PostChatService postChatService;
	private final SimpMessagingTemplate messagingTemplate;

	@GetMapping("/{chatroomId}/messages")
	@Operation(summary = "채팅 조회", description = "채팅방의 채팅을 Slice로 조회하는 Api(기본 20개, 더 조회할 수 있으면 hasNext가 true)")
	public ResponseEntity<DataResponse<ChatMessageListResponse>> getChatMessages(@PathVariable Long chatroomId,
		@RequestParam(required = false, defaultValue = "1") int page) {
		ChatMessageSliceSnapshot messages = postChatService.getMessages(page, chatroomId);
		return ResponseEntity.ok(DataResponse.from(
				ChatMessageListResponse.from(messages)
			)
		);
	}

	@MessageMapping("/chatrooms/{chatroomId}")
	@Operation(summary = "채팅 보내기")
	public void sendMessage(@DestinationVariable Long chatroomId, @Payload ChatMessageRequest request) {
		ChatNotificationResult result = postChatService.processNewMessage(chatroomId, request.message());

		// 채팅방 채널로 메시지 전송
		messagingTemplate.convertAndSend(
			"/chatrooms/" + chatroomId,
			result.chatMessageEntity()
		);

		// 수신자의 알림 채널로 알림 전송
		messagingTemplate.convertAndSendToUser(
			result.receiverId().toString(),
			"/notifications/chat",
			result.notification()
		);
	}

	@PostMapping("/{chatroomId}/read")
	@Operation(summary = "채팅방 메시지 읽음 처리", description = "채팅방의 모든 메시지를 읽음 처리합니다.")
	public ResponseEntity<Void> markChatroomAsRead(@PathVariable Long chatroomId) {
		postChatService.markMessagesAsRead(chatroomId);
		return ResponseEntity.ok().build();
	}
}