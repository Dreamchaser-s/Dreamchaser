package com.boardtest.dreamchaser.admin;

import com.boardtest.dreamchaser.post.Post;
import com.boardtest.dreamchaser.post.PostService;
import com.boardtest.dreamchaser.user.User;
import com.boardtest.dreamchaser.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@RequestMapping("/admin")
@RequiredArgsConstructor
@Controller
@PreAuthorize("hasRole('ADMIN')") // 이 컨트롤러의 모든 메소드는 ADMIN 역할만 접근 가능
public class AdminController {

    private final UserService userService;
    private final PostService postService; // ★★★ PostService 주입 추가 ★★★

    // 사용자 목록 페이지
    @GetMapping("/users")
    public String userList(Model model, @PageableDefault(size = 10, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<User> userPaging = userService.findUsers(pageable);
        model.addAttribute("userPaging", userPaging);
        return "admin/user_list";
    }

    // 기간제 차단 처리
    @PostMapping("/users/ban/temp/{id}")
    public String banTemporarily(@PathVariable("id") Long id, @RequestParam(defaultValue = "7") int days, Principal principal) {
        User user = userService.getUserById(id);
        if (!user.getUsername().equals(principal.getName())) {
            userService.banTemporarily(user, days);
        }
        return "redirect:/admin/users";
    }

    // 영구 차단 처리
    @PostMapping("/users/ban/perm/{id}")
    public String banPermanently(@PathVariable("id") Long id, Principal principal) {
        User user = userService.getUserById(id);
        if (!user.getUsername().equals(principal.getName())) {
            userService.banPermanently(user);
        }
        return "redirect:/admin/users";
    }

    // 차단 해제 처리
    @PostMapping("/users/unban/{id}")
    public String unban(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        userService.unban(user);
        return "redirect:/admin/users";
    }

    // 승인 대기중인 게시글 목록 페이지
    @GetMapping("/posts/pending")
    public String pendingList(Model model, @PageableDefault(size = 10, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> pendingPaging = this.postService.findPendingPosts(pageable);
        model.addAttribute("pendingPaging", pendingPaging);
        return "admin/pending_posts";
    }

    // 게시글을 '승인' 처리
    @PostMapping("/posts/approve/{id}")
    public String approvePost(@PathVariable("id") Long id) {
        postService.approvePost(id);
        return "redirect:/admin/posts/pending"; // 처리 후 다시 대기 목록으로
    }

    // 게시글을 '거절' 처리
    @PostMapping("/posts/reject/{id}")
    public String rejectPost(@PathVariable("id") Long id) {
        postService.rejectPost(id);
        return "redirect:/admin/posts/pending";
    }
}