package com.cotato.kampus.domain.chat.application;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.domain.Chatroom;
import com.cotato.kampus.domain.chat.dto.ChatRoomPreview;
import com.cotato.kampus.domain.chat.dto.ChatRoomPreviewList;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.application.PostFinder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostChatService {

	private final ChatRoomValidator chatRoomValidator;
	private final ChatRoomAppender chatRoomAppender;
	private final ApiUserResolver apiUserResolver;
	private final PostFinder postFinder;
	private final ChatRoomFinder chatRoomFinder;
	private final ChatRoomMapper chatRoomMapper;

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

		// 3. 채팅방의 첫 번째 메시지와 채팅 시간 필드를 추가하여 매핑
		List<ChatRoomPreview> previewList = chatRooms.getContent().stream()
			.map(chatRoomMapper::toChatRoomPreview)
			.toList();

		return ChatRoomPreviewList.from(previewList, chatRooms.hasNext());
	}
}