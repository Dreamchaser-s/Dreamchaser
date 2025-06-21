package com.boardtest.dreamchaser.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import com.boardtest.dreamchaser.user.User;
import java.util.List;
import com.boardtest.dreamchaser.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthor(User author);

    Page<Comment> findByPostAndParentIsNull(Post post, Pageable pageable);


    long countByPost(Post post);
}