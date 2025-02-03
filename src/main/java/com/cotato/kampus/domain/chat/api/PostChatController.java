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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cotato.kampus.domain.chat.application.ChatMessageService;
import com.cotato.kampus.domain.chat.application.PostChatService;
import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.cotato.kampus.domain.chat.dto.ChatMessageSliceWithUserId;
import com.cotato.kampus.domain.chat.dto.request.ChatMessageRequest;
import com.cotato.kampus.domain.chat.dto.request.ChatroomRequest;
import com.cotato.kampus.domain.chat.dto.response.ChatMessageListResponse;
import com.cotato.kampus.domain.chat.dto.response.ChatroomResponse;
import com.cotato.kampus.global.common.dto.DataResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/chats")
public class PostChatController {

	private final PostChatService postChatService;
	private final ChatMessageService chatMessageService;
	private final SimpMessagingTemplate messagingTemplate;

	@PostMapping("/post")
	@ResponseBody
	public ResponseEntity<DataResponse<ChatroomResponse>> createChatroom(@RequestBody ChatroomRequest request) {
		return ResponseEntity.ok(DataResponse.from(
				ChatroomResponse.of(
					postChatService.createChatRoom(
						request.postId()
					)
				)
			)
		);
	}

	@GetMapping("/{chatroomId}/messages")
	@ResponseBody
	public ResponseEntity<DataResponse<ChatMessageListResponse>> getChatMessages(@PathVariable Long chatroomId,
		@RequestParam(required = false, defaultValue = "1") int page) {
		ChatMessageSliceWithUserId messages = chatMessageService.getMessages(page, chatroomId);
		return ResponseEntity.ok(DataResponse.from(
				ChatMessageListResponse.from(messages)
			)
		);
	}

	@MessageMapping("/chatrooms/{chatroomId}")
	public void sendMessage(@DestinationVariable Long chatroomId, @Payload ChatMessageRequest request) {
		ChatMessage chatMessage = chatMessageService.saveMessage(request.chatroomId(), request.message());
		messagingTemplate.convertAndSend("/chatrooms/" + chatroomId, chatMessage);
	}
}