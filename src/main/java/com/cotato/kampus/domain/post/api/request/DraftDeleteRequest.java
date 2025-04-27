package com.cotato.kampus.domain.post.api.request;

import java.util.List;

public record DraftDeleteRequest(
	List<Long> draftPostIds
) {
}
