package com.cotato.kampus.domain.chat.application;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.chat.api.port.ChatRoomService;
import com.cotato.kampus.domain.chat.domain.ChatReference;
import com.cotato.kampus.domain.chat.domain.ChatRoom;
import com.cotato.kampus.domain.chat.domain.ChatRoomDetailDto;
import com.cotato.kampus.domain.chat.domain.ChatRoomPreview;
import com.cotato.kampus.domain.chat.domain.ChatRoomPreviewList;
import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.domain.chat.implement.ReferenceFinder;
import com.cotato.kampus.domain.chat.implement.chatroom.ChatRoomAppender;
import com.cotato.kampus.domain.chat.implement.chatroom.ChatRoomDeleter;
import com.cotato.kampus.domain.chat.implement.chatroom.ChatRoomFinder;
import com.cotato.kampus.domain.chat.implement.chatroom.ChatRoomValidator;
import com.cotato.kampus.domain.chat.implement.message.ChatMessageDeleter;
import com.cotato.kampus.domain.chat.implement.metadata.ChatroomMetadataAppender;
import com.cotato.kampus.domain.chat.implement.metadata.ChatroomMetadataDeleter;
import com.cotato.kampus.domain.chat.implement.metadata.ChatroomMetadataFinder;
import com.cotato.kampus.domain.chat.implement.metadata.ChatroomMetadataMapper;
import com.cotato.kampus.domain.chat.implement.read.MessageReadStatusDeleter;
import com.cotato.kampus.domain.common.application.ApiUserResolver;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {

	private final ChatRoomValidator chatRoomValidator;
	private final ChatRoomAppender chatRoomAppender;
	private final ChatRoomFinder chatRoomFinder;
	private final ChatRoomDeleter chatroomDeleter;

	private final ApiUserResolver apiUserResolver;

	private final BoardFinder boardFinder;

	private final ChatMessageDeleter chatMessageDeleter;
	private final MessageReadStatusDeleter messageReadStatusDeleter;

	private final ChatroomMetadataAppender chatroomMetadataAppender;
	private final ChatroomMetadataFinder chatroomMetadataFinder;
	private final ChatroomMetadataMapper chatroomMetadataMapper;
	private final ChatroomMetadataDeleter chatroomMetadataDeleter;

	private final ReferenceFinder referenceFinder;

	@Override
	@Transactional
	public Long createChatRoom(Long referenceId, ChatType chatType) {

		ChatReference chatReference = referenceFinder.find(referenceId, chatType);

		// 2. 채팅을 건 유저를 조회
		Long senderId = apiUserResolver.getCurrentUserId();

		// 3. 채팅방 중복 검증(이미 있는 채팅방)
		chatRoomValidator.validateDuplicateChatRoom(chatReference.getReferenceId(), senderId);

		// 4. 채팅방 생성(생성 시 검증 이루어짐(sender != receiver))
		Long chatroomId = chatRoomAppender.appendChatRoom(chatReference.getReferenceUserId(), chatType, senderId,
			chatReference.getReferenceUserId());

		// 5. 채팅방 리스트 조회시 사용되는 뷰 생성
		chatroomMetadataAppender.createMetadataPair(chatroomId, chatType, referenceId, chatReference.getTitle(),
			senderId, chatReference.getReferenceUserId());

		return chatroomId;
	}

	@Override
	public ChatRoomPreviewList findChatRooms(int page, ChatType chatType) {
		// 1. 유저 정보를 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 2. 해당 유저의 채팅방 메타데이터를 lastChatTime 내림차순으로 조회
		Slice<ChatroomMetadata> chatRoomMetadatas = chatroomMetadataFinder.findChatRoomMetadatas(userId, page,
			chatType);

		// 3. ChatRoomPreview로 변환
		List<ChatRoomPreview> previewList = chatRoomMetadatas.getContent()
			.stream()
			.map(chatroomMetadataMapper::toChatRoomPreview)
			.toList();

		return ChatRoomPreviewList.from(previewList, chatRoomMetadatas.hasNext());
	}

	@Override
	public ChatRoomDetailDto getChatRoomDetail(Long chatroomId) {
		// 1. Find chatroom
		ChatRoom chatRoom = chatRoomFinder.findByChatRoomId(chatroomId);

		// 2. 채팅방 타입 가져오기
		ChatType chatType = chatRoom.getChatType();

		// 3. 참조 정보 가져옴 (게시글 또는 상품 등)
		ChatReference reference = referenceFinder.find(chatRoom.getReferenceId(), chatType);

		// 4. 참조 정보가 없는 경우 (삭제된 경우)
		if (reference.isDeleted()) {
			return ChatRoomDetailDto.ofDeleted(chatRoom, reference);
		}

		// 5. 게시글이 존재하는 경우
		if (chatType == ChatType.POST) {
			Board board = boardFinder.findBoard(reference.getBoardId());
			return ChatRoomDetailDto.of(chatRoom, reference, board);
		}

		// 6. 기타 타입 (product 등) - 현재는 구현 필요 없음
		return ChatRoomDetailDto.of(chatRoom, reference, null);
	}

	@Override
	@Transactional
	public void deleteChatroom(Long chatroomId) {
		// 1. 현재 사용자 ID 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 2. 채팅방 멤버 검증
		chatRoomValidator.validateEnteredUser(userId, chatroomId);

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
