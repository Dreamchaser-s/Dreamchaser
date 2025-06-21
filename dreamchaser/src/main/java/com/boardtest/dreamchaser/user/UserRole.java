package com.boardtest.dreamchaser.user;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN", "관리자"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String displayName;

    UserRole(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }
}