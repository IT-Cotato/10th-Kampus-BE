package com.cotato.kampus.domain.chat.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cotato.kampus.domain.chat.api.port.ChatRoomService;
import com.cotato.kampus.domain.chat.api.request.ChatroomRequest;
import com.cotato.kampus.domain.chat.api.response.ChatRoomDetailResponse;
import com.cotato.kampus.domain.chat.api.response.ChatRoomListResponse;
import com.cotato.kampus.domain.chat.api.response.ChatroomResponse;
import com.cotato.kampus.domain.chat.api.validator.ValidChatType;
import com.cotato.kampus.domain.chat.domain.ChatRoomPreviewList;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시글 채팅 API", description = "게시글 채팅 API")
@Controller
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/chats")
public class PostChatRoomController {

	private final ChatRoomService chatRoomService;

	@PostMapping("/post")
	@Operation(summary = "채팅방 생성", description = "채팅방 생성 요청입니다.")
	@ResponseBody
	public ResponseEntity<DataResponse<ChatroomResponse>> createChatroom(@RequestBody @Valid ChatroomRequest request,
		@RequestParam(required = true, name = "type") @ValidChatType String chatType) {
		return ResponseEntity.ok(DataResponse.from(
			ChatroomResponse.of(chatRoomService.createChatRoom(request.referenceId(), ChatType.valueOf(chatType)))));
	}

	@GetMapping("/chatrooms")
	@Operation(summary = "내가 속한 채팅방 조회", description = "현재 참여중인 채팅방을 조회합니다.")
	public ResponseEntity<DataResponse<ChatRoomListResponse>> getChatRooms(
		@RequestParam(required = false, defaultValue = "1") int page,
		@RequestParam(required = false, name = "type") @ValidChatType String chatType) {
		ChatRoomPreviewList chatRooms = chatRoomService.findChatRooms(page, ChatType.valueOf(chatType));
		return ResponseEntity.ok(DataResponse.from(ChatRoomListResponse.from(chatRooms)));
	}

	@GetMapping("/chatrooms/{chatroomId}")
	@Operation(summary = "채팅방 상세 조회", description = "채팅방의 상세 정보를 조회합니다.")
	@ResponseBody
	public ResponseEntity<DataResponse<ChatRoomDetailResponse>> getChatRoomDetail(@PathVariable Long chatroomId,
		@RequestParam(required = true, name = "type") @ValidChatType String chatType) {
		return ResponseEntity.ok(DataResponse.from(
			ChatRoomDetailResponse.from(chatRoomService.getChatRoomDetail(chatroomId, ChatType.valueOf(chatType)))));
	}

	@DeleteMapping("/chatrooms/{chatroomId}")
	@Operation(summary = "채팅방 삭제", description = "채팅방과 관련된 모든 데이터(메시지, 읽음 상태, 메타데이터)를 삭제합니다.")
	@ResponseBody
	public ResponseEntity<DataResponse<Void>> deleteChatroom(@PathVariable Long chatroomId) {
		chatRoomService.deleteChatroom(chatroomId);
		return ResponseEntity.ok(DataResponse.ok());
	}
}