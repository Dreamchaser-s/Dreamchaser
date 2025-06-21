package com.boardtest.dreamchaser.post;

import com.boardtest.dreamchaser.category.Category;
import com.boardtest.dreamchaser.comment.Comment;
import com.boardtest.dreamchaser.comment.CommentService;
import com.boardtest.dreamchaser.user.User;
import com.boardtest.dreamchaser.user.UserRole;
import com.boardtest.dreamchaser.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList; // ArrayList import 추가
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/post")
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;


    // --- 목록, 검색 등 GET 메소드 (수정 없음) ---
    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 12, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(value = "sortCode", defaultValue = "latest") String sortCode) {
        List<Post> recentList = this.postService.getRecentList(6);
        Page<Post> originalPaging = this.postService.getList(pageable, sortCode);

        // PersistentBag -> ArrayList 변환
        List<Post> standardList = new ArrayList<>(originalPaging.getContent());
        Page<Post> paging = new PageImpl<>(standardList, pageable, originalPaging.getTotalElements());

        model.addAttribute("recentList", recentList);
        model.addAttribute("paging", paging);
        model.addAttribute("keyword", "");
        model.addAttribute("sortCode", sortCode);
        model.addAttribute("isSearch", false);
        model.addAttribute("pageTitle", "전체 리뷰");
        return "post_list";
    }


    @GetMapping("/list/by_category")
    public String listByCategory(Model model, @RequestParam("category") Category category,
                                 @PageableDefault(size = 12, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable,
                                 @RequestParam(value = "sortCode", defaultValue = "latest") String sortCode) {
        Page<Post> originalPaging = this.postService.findByCategory(category, pageable, sortCode);

        List<Post> standardList = new ArrayList<>(originalPaging.getContent());
        Page<Post> paging = new PageImpl<>(standardList, pageable, originalPaging.getTotalElements());

        model.addAttribute("paging", paging);
        model.addAttribute("sortCode", sortCode);
        model.addAttribute("keyword", "");
        model.addAttribute("isSearch", true);
        model.addAttribute("pageTitle", "'" + category.getDisplayName() + "' 카테고리 리뷰");
        return "post_list";
    }


    @GetMapping("/search")
    public String search(Model model, @RequestParam(value = "q", defaultValue = "") String keyword,
                         @PageableDefault(size = 12, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam(value = "sortCode", defaultValue = "latest") String sortCode) {
        Page<Post> originalPaging = this.postService.search(keyword, pageable, sortCode);

        List<Post> standardList = new ArrayList<>(originalPaging.getContent());
        Page<Post> paging = new PageImpl<>(standardList, pageable, originalPaging.getTotalElements());

        model.addAttribute("paging", paging);
        model.addAttribute("sortCode", sortCode);
        model.addAttribute("keyword", keyword);
        model.addAttribute("isSearch", true);
        model.addAttribute("pageTitle", "'" + keyword + "' 검색 결과");
        return "post_list";
    }

    // ★★★ Lazy-Loading 및 PersistentBag 문제를 해결한 최종 detail 메소드 ★★★
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id, Principal principal) {
        Post post = this.postService.getPost(id);

        if (post.getStatus() != PostStatus.APPROVED) {
            if (principal == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 후 조회해주세요.");
            }
            boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(UserRole.ADMIN.getKey()));
            if (!post.getAuthor().getUsername().equals(principal.getName()) && !isAdmin) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이 게시글을 조회할 권한이 없습니다.");
            }
        }

        this.postService.incrementViewCount(id);

        // 전체 댓글 개수는 여전히 필요
        long totalCommentCount = this.commentService.getCommentCount(post);
        model.addAttribute("totalCommentCount", totalCommentCount);

        model.addAttribute("post", post); // Post 객체만 전달

        return "post_detail";
    }


    // --- 글쓰기/수정 폼 GET 메소드들 (수정 없음) ---
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String postCreate(Model model) {
        model.addAttribute("postForm", new PostForm());
        Map<String, List<Category>> groupedCategories = Arrays.stream(Category.values())
                .collect(Collectors.groupingBy(Category::getMainCategory));
        model.addAttribute("groupedCategories", groupedCategories);
        return "post_form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String postModify(PostForm postForm, @PathVariable("id") Long id, Model model, Principal principal) {
        Post post = this.postService.getPost(id);
        if (!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        postForm.setTitle(post.getTitle());
        postForm.setCategory(post.getCategory());
        postForm.setStarRating(post.getStarRating());
        postForm.setSummary(post.getSummary());
        postForm.setPros(post.getPros());
        postForm.setCons(post.getCons());
        postForm.setContent(post.getContent());
        Map<String, List<Category>> groupedCategories = Arrays.stream(Category.values())
                .collect(Collectors.groupingBy(Category::getMainCategory));
        model.addAttribute("groupedCategories", groupedCategories);
        model.addAttribute("post", post);
        return "post_form";
    }

    // --- 글쓰기/수정/삭제/추천 등 작업을 처리하는 메소드들 (수정 없음) ---
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String postCreate(@Valid PostForm postForm, BindingResult bindingResult, Model model, Principal principal) throws IOException {
        if (bindingResult.hasErrors()) {
            Map<String, List<Category>> groupedCategories = Arrays.stream(Category.values())
                    .collect(Collectors.groupingBy(Category::getMainCategory));
            model.addAttribute("groupedCategories", groupedCategories);
            return "post_form";
        }
        User user = this.userService.getUser(principal.getName());
        this.postService.create(postForm.getTitle(), postForm.getContent(), user, postForm.getCategory(),
                postForm.getStarRating(), postForm.getSummary(), postForm.getPros(), postForm.getCons(), postForm.getImageFile());
        return "redirect:/post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String postModify(@Valid PostForm postForm, BindingResult bindingResult,
                             @PathVariable("id") Long id, Model model, Principal principal) throws IOException {
        Post post = this.postService.getPost(id);
        if (bindingResult.hasErrors()) {
            Map<String, List<Category>> groupedCategories = Arrays.stream(Category.values())
                    .collect(Collectors.groupingBy(Category::getMainCategory));
            model.addAttribute("groupedCategories", groupedCategories);
            model.addAttribute("post", post);
            return "post_form";
        }
        if (!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        this.postService.modify(post, postForm.getTitle(), postForm.getContent(), postForm.getCategory(),
                postForm.getStarRating(), postForm.getSummary(), postForm.getPros(), postForm.getCons(), postForm.getImageFile());
        return String.format("redirect:/post/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String postDelete(@PathVariable("id") Long id, Principal principal) {
        Post post = this.postService.getPost(id);
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(UserRole.ADMIN.getKey()));
        if (!isAdmin && !post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        this.postService.delete(post);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String postVote(@PathVariable("id") Long id, Principal principal) {
        Post post = this.postService.getPost(id);
        User user = this.userService.getUser(principal.getName());
        this.postService.vote(post, user);
        return String.format("redirect:/post/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/image-upload")
    @ResponseBody
    public Map<String, String> uploadEditorImage(@RequestParam("file") MultipartFile imageFile) {
        try {
            String savedFilename = postService.saveFile(imageFile);
            String imageUrl = "/uploads/" + savedFilename;
            Map<String, String> response = new HashMap<>();
            response.put("url", imageUrl);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }
    }

}
