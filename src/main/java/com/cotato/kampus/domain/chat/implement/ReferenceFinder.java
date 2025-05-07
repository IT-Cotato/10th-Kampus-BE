package com.cotato.kampus.domain.chat.implement;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.domain.ChatReference;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.implement.post.PostFinder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReferenceFinder {

	private final PostFinder postFinder;

	public ChatReference find(final Long referenceId, ChatType chatType) {
		if (chatType == ChatType.POST) {
			Post post = postFinder.find(referenceId);
			return ChatReference.builder()
				.referenceId(post.getId())
				.referenceUserId(post.getUserId())
				.title(post.getTitle())
				.build();
		} else {
			// TODO: Handle other chat types
			return ChatReference.builder()
				.referenceId(referenceId)
				.referenceUserId(null)
				.title(null)
				.build();
		}
	}
}