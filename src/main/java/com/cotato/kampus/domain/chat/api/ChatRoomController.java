package com.cotato.kampus.domain.chat.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.chat.api.port.ChatRoomService;
import com.cotato.kampus.domain.chat.api.request.ChatroomRequest;
import com.cotato.kampus.domain.chat.api.response.ChatRoomDetailResponse;
import com.cotato.kampus.domain.chat.api.response.ChatRoomListResponse;
import com.cotato.kampus.domain.chat.api.response.ChatroomResponse;
import com.cotato.kampus.domain.chat.api.validator.ValidChatType;
import com.cotato.kampus.domain.chat.domain.ChatRoomPreviewList;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.global.common.dto.DataResponse;
import com.cotato.kampus.global.error.response.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "채팅방 API", description = "채팅방 생성, 조회, 삭제 관련 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/chats/chatrooms")
public class ChatRoomController {

	private final ChatRoomService chatRoomService;

	@PostMapping
	@Operation(
		summary = "채팅방 생성",
		description = "채팅방 생성 요청입니다.",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "성공"
			),
			@ApiResponse(
				responseCode = "400",
				description = "요청 파라미터가 잘못되었습니다.[COMMON-002], 이미 존재하는 채팅 방입니다.[CHAT-002]",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "401",
				description = "인증 토큰에 문제가 있습니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "403",
				description = "자신에게 채팅을 할 수 없습니다.[CHAT-001]",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "500",
				description = "서버 내부 오류가 발생했습니다.[COMMON-002]",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			)
		}
	)
	public ResponseEntity<DataResponse<ChatroomResponse>> createChatroom(@RequestBody @Valid ChatroomRequest request,
		@RequestParam(required = false, name = "type") @ValidChatType String chatType) {
		return ResponseEntity.ok(DataResponse.from(
			ChatroomResponse.of(chatRoomService.createChatRoom(request.referenceId(), ChatType.valueOf(chatType)))));
	}

	@GetMapping
	@Operation(
		summary = "내가 속한 채팅방 조회",
		description = "현재 참여중인 채팅방을 조회합니다. type 파라미터가 없으면 전체 채팅방을 조회합니다.",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "성공"
			),
			@ApiResponse(
				responseCode = "400",
				description = "요청 파라미터가 잘못되었습니다.[COMMON-002], 채팅방 타입이 일치하지 않습니다.[CHAT-007]",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "401",
				description = "인증 토큰에 문제가 있습니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "500",
				description = "서버 내부 오류가 발생했습니다.[COMMON-002]",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			)
		}
	)
	public ResponseEntity<DataResponse<ChatRoomListResponse>> getChatRooms(
		@RequestParam(required = false, defaultValue = "1") int page,
		@RequestParam(required = false, name = "type", defaultValue = "ALL") @ValidChatType String chatType) {
		ChatRoomPreviewList chatRooms = chatRoomService.findChatRooms(page, ChatType.valueOf(chatType));
		return ResponseEntity.ok(DataResponse.from(ChatRoomListResponse.from(chatRooms)));
	}

	@GetMapping("/{chatroomId}")
	@Operation(
		summary = "채팅방 상세 조회",
		description = "채팅방의 상세 정보를 조회합니다.",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "성공"
			),
			@ApiResponse(
				responseCode = "400",
				description = "요청 파라미터가 잘못되었습니다.[COMMON-002]",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "401",
				description = "인증 토큰에 문제가 있습니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "403",
				description = "채팅방에 입장한 유저가 아닙니다.[CHAT-004]",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "404",
				description = "해당 채팅방을 찾을 수 없습니다.[CHAT-003]",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "500",
				description = "서버 내부 오류가 발생했습니다.[COMMON-002]",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			)
		}
	)
	public ResponseEntity<DataResponse<ChatRoomDetailResponse>> getChatRoomDetail(@PathVariable Long chatroomId) {
		return ResponseEntity.ok(
			DataResponse.from(ChatRoomDetailResponse.from(chatRoomService.getChatRoomDetail(chatroomId))));
	}

	@DeleteMapping("/{chatroomId}")
	@Operation(
		summary = "채팅방 삭제",
		description = "채팅방과 관련된 모든 데이터(메시지, 읽음 상태, 메타데이터)를 삭제합니다.",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "성공"
			),
			@ApiResponse(
				responseCode = "400",
				description = "요청 파라미터가 잘못되었습니다.[COMMON-002]",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "401",
				description = "인증 토큰에 문제가 있습니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "403",
				description = "채팅방에 입장한 유저가 아닙니다.[CHAT-004]",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "404",
				description = "해당 채팅방을 찾을 수 없습니다.[CHAT-003], 해당 채팅방 메타데이터를 찾을 수 없습니다.[CHAT-006]",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "500",
				description = "서버 내부 오류가 발생했습니다.[COMMON-002]",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			)
		}
	)
	public ResponseEntity<DataResponse<Void>> deleteChatroom(@PathVariable Long chatroomId) {
		chatRoomService.deleteChatroom(chatroomId);
		return ResponseEntity.ok(DataResponse.ok());
	}
}