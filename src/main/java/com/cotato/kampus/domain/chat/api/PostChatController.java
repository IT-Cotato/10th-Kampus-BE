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
import com.cotato.kampus.domain.chat.dto.ChatRoomPreviewList;
import com.cotato.kampus.domain.chat.dto.request.ChatMessageRequest;
import com.cotato.kampus.domain.chat.dto.request.ChatroomRequest;
import com.cotato.kampus.domain.chat.dto.response.ChatMessageListResponse;
import com.cotato.kampus.domain.chat.dto.response.ChatRoomDetailResponse;
import com.cotato.kampus.domain.chat.dto.response.ChatRoomListResponse;
import com.cotato.kampus.domain.chat.dto.response.ChatroomResponse;
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
	private final ChatMessageService chatMessageService;
	private final SimpMessagingTemplate messagingTemplate;

	@PostMapping("/post")
	@Operation(summary = "채팅방 생성", description = "채팅방 생성 요청입니다.")
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
	@Operation(summary = "채팅 조회", description = "채팅방의 채팅을 Slice로 조회하는 Api(기본 20개, 더 조회할 수 있으면 hasNext가 true)")
	public ResponseEntity<DataResponse<ChatMessageListResponse>> getChatMessages(@PathVariable Long chatroomId,
		@RequestParam(required = false, defaultValue = "1") int page) {
		ChatMessageSliceWithUserId messages = chatMessageService.getMessages(page, chatroomId);
		return ResponseEntity.ok(DataResponse.from(
				ChatMessageListResponse.from(messages)
			)
		);
	}

	@MessageMapping("/chatrooms/{chatroomId}")
	@Operation(summary = "채팅 보내기", description = "websocket 연결 후, 채팅 메시지 보내는 요청")
	public void sendMessage(@DestinationVariable Long chatroomId, @Payload ChatMessageRequest request) {
		ChatMessage chatMessage = chatMessageService.saveMessage(request.chatroomId(), request.message());
		messagingTemplate.convertAndSend("/chatrooms/" + chatroomId, chatMessage);
	}

	@GetMapping("/chatrooms")
	@Operation(summary = "내가 속한 채팅방 조회", description = "현재 참여중인 채팅방을 조회합니다.")
	public ResponseEntity<DataResponse<ChatRoomListResponse>> getChatRooms(
		@RequestParam(required = false, defaultValue = "1") int page
	) {
		ChatRoomPreviewList chatRooms = postChatService.findChatRooms(page);
		return ResponseEntity.ok(
			DataResponse.from(
				ChatRoomListResponse.from(chatRooms)
			)
		);
	}

	@GetMapping("/chatrooms/{chatroomId}")
	@Operation(summary = "채팅방 상세 조회", description = "채팅방의 상세 정보를 조회합니다.")
	@ResponseBody
	public ResponseEntity<DataResponse<ChatRoomDetailResponse>> getChatRoomDetail(
		@PathVariable Long chatroomId
	) {
		return ResponseEntity.ok(DataResponse.from(
				ChatRoomDetailResponse.from(
					postChatService.getChatRoomDetail(chatroomId)
				)
			)
		);
	}
}