package com.boardtest.dreamchaser.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import com.boardtest.dreamchaser.post.Post;
import com.boardtest.dreamchaser.comment.Comment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import java.util.List;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username; // 사용자 로그인 아이디

    private String password; // 사용자 비밀번호

    @Column(unique = true) // 이 필드의 값은 유일해야 합니다. (중복 닉네임 방지)
    private String nickname; // 닉네임

    private String profileImage; // 프로필 사진 경로 (선택 사항)

    private LocalDateTime createDate; // 가입일

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Post> postList;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean permanentlyBanned = false; // 영구 차단 여부
    private LocalDateTime bannedUntil; // 기간제 차단 만료 일시
}