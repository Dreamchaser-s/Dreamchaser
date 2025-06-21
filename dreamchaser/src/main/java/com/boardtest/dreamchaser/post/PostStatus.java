package com.boardtest.dreamchaser.post;

import lombok.Getter;

@Getter
public enum PostStatus {
    PENDING("승인 대기"),
    APPROVED("게시 승인"),
    REJECTED("게시 거절");

    private final String displayName;

    PostStatus(String displayName) {
        this.displayName = displayName;
    }
}