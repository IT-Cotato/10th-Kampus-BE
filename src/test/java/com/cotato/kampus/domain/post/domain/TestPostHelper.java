package com.cotato.kampus.domain.post.domain;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.enums.PostStatus;
import java.time.LocalDateTime;

public class TestPostHelper {

    private Long id = 1L;
    private Long boardId = 1L;
    private Long userId = 1L;
    private String title = "test title";
    private String content = "test content";
    private PostStatus postStatus = PostStatus.PUBLISHED;
    private Anonymity anonymity = Anonymity.ANONYMOUS;
    private int likeCount = 0;
    private int commentCount = 0;
    private int scrapCount = 0;
    private int anonymousCount = 0;

    public TestPostHelper withId(Long id) {
        this.id = id;
        return this;
    }

    public TestPostHelper withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public TestPostHelper withCommentCount(int commentCount) {
        this.commentCount = commentCount;
        return this;
    }

    public TestPostHelper withAnonymousCount(int anonymousCount) {
        this.anonymousCount = anonymousCount;
        return this;
    }

    public TestPostHelper withLikeCount(int likeCount) {
        this.likeCount = likeCount;
        return this;
    }

    public Post createNormalPost() {
        return new NormalPost(
            this.id,
            this.boardId,
            this.userId,
            this.title,
            this.content,
            this.postStatus,
            this.anonymity,
            this.likeCount,
            this.commentCount,
            this.scrapCount,
            this.anonymousCount,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    public Post createCardNewsPost() {
        return new CardNewsPost(
            this.id,
            this.boardId,
            this.userId,
            this.title,
            this.content,
            this.postStatus,
            this.anonymity,
            this.likeCount,
            this.commentCount,
            this.scrapCount,
            this.anonymousCount,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }
}
