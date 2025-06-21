package com.boardtest.dreamchaser;

import com.boardtest.dreamchaser.post.Post;
import com.boardtest.dreamchaser.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final PostService postService; // PostService 주입

    @GetMapping("/")
    public String root(Model model) { // Model 파라미터 추가
        // 최신 리뷰 6개를 가져온다.
        List<Post> postList = this.postService.getRecentList(6);
        model.addAttribute("postList", postList); // 뷰로 데이터 전달
        return "main";
    }
}