package com.boardtest.dreamchaser.comment;

import com.boardtest.dreamchaser.post.Post;
import com.boardtest.dreamchaser.post.PostService;
import com.boardtest.dreamchaser.user.User;
import com.boardtest.dreamchaser.user.UserRole;
import com.boardtest.dreamchaser.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;



    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createComment(@PathVariable("id") Long id, @RequestParam String content,
                                @RequestParam(value = "parentId", required = false) Long parentId,
                                Principal principal) {

        Post post = this.postService.getPost(id);
        if (this.commentService.getCommentCount(post) >= 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "더 이상 댓글을 작성할 수 없습니다.");
        }
        User author = this.userService.getUser(principal.getName());

        Comment parent = null;
        if (parentId != null) {
            parent = this.commentService.getComment(parentId);
        }


        this.commentService.create(post, content, author, parent);

        return String.format("redirect:/post/detail/%s", id);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String commentDelete(@PathVariable("id") Long id, Principal principal) {
        Comment comment = this.commentService.getComment(id);

        // ★★★ 현재 사용자가 ADMIN 역할인지 확인하는 로직 추가 ★★★
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(UserRole.ADMIN.getKey()));

        // 관리자가 아니고, 댓글 작성자도 아닐 경우에만 권한 없음 예외 발생
        if (!isAdmin && !comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }

        Long postId = comment.getPost().getId();
        this.commentService.delete(comment);
        return String.format("redirect:/post/detail/%s", postId);
    }
}