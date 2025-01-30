package com.cotato.kampus.domain.post.dto.request;

import java.util.List;

public record DraftDeleteRequest(
	List<Long> draftPostIds
) {
}
