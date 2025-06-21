package com.boardtest.dreamchaser.comment;

import com.boardtest.dreamchaser.post.Post;
import com.boardtest.dreamchaser.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User author;

    @ManyToOne
    private Comment parent;

    // ★★★ EAGER 로딩 방식으로 변경, 연쇄 삭제 옵션을 모두 설정합니다. ★★★
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();
}