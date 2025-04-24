package com.cotato.kampus.domain.post.implement.port;

import com.cotato.kampus.domain.post.domain.TemporaryPost;

public interface TemporaryPostRepository {

	TemporaryPost save(TemporaryPost temporaryPost);
}
