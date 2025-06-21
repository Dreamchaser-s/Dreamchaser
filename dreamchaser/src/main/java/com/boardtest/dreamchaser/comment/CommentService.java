package com.boardtest.dreamchaser.comment;

import com.boardtest.dreamchaser.post.Post;
import com.boardtest.dreamchaser.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void create(Post post, String content, User author, Comment parent) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setParent(parent);
        this.commentRepository.save(comment);
    }

    public Comment getComment(Long id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }

    public List<Comment> findByAuthor(User author) {
        return this.commentRepository.findByAuthor(author);
    }

    // ★★★ 페이징 관련 메소드들은 이제 detail 페이지에서 직접 사용하지 않습니다. ★★★
    public Page<Comment> getCommentPage(Post post, Pageable pageable) {
        return commentRepository.findByPostAndParentIsNull(post, pageable);
    }

    public long getCommentCount(Post post) {
        return commentRepository.countByPost(post);
    }

    // ★★★ 재귀 로딩 메소드는 EAGER 방식으로 대체되었으므로 삭제합니다. ★★★
}
