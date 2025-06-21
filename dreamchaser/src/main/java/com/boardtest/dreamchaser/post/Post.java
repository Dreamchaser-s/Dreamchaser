package com.boardtest.dreamchaser.post;

import com.boardtest.dreamchaser.category.Category;
import com.boardtest.dreamchaser.comment.Comment;
import com.boardtest.dreamchaser.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Getter
@Setter
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    // ★★★ 가장 중요한 부분 ★★★
    // 여러 개의 게시글(Post)이 한 명의 사용자(User)에게 속할 수 있다.
    @ManyToOne
    private User author; // 작성자

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int viewCount;

    // ★★★ 이 게시글에 달린 댓글들의 목록 ★★★
    // mappedBy = "post": Comment 엔티티의 'post' 필드에 의해 관계가 관리됨을 의미
    // CascadeType.REMOVE: 이 게시글(Post)이 삭제될 경우, 관련된 댓글(Comment)들도 모두 함께 삭제됨
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @Enumerated(EnumType.STRING) // DB에 저장될 때 Enum의 이름을 문자열로 저장
    private Category category; // 예: "노트북", "스마트폰", "키보드" 등

    private int starRating; // 1 ~ 5점 사이의 별점

    @Column(length = 300)
    private String summary; // 한줄평

    @Column(columnDefinition = "TEXT")
    private String pros; // 장점 (리스트 형식으로 입력받을 수 있음)

    @Column(columnDefinition = "TEXT")
    private String cons; // 단점

    private String imageUrl; // 대표 이미지 URL

    // ★★★ 이 게시글을 추천한 사람들의 목록 ★★★
    // 한 명의 사용자(User)는 여러 개의 게시글(Post)을 추천할 수 있고,
    // 하나의 게시글(Post)은 여러 명의 사용자(User)로부터 추천받을 수 있다.
    @ManyToMany
    Set<User> voter; // 추천인. Set을 사용하면 중복 없이 저장 가능

    @Enumerated(EnumType.STRING)
    private PostStatus status;

}