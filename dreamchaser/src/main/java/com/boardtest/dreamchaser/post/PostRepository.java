package com.boardtest.dreamchaser.post;

import com.boardtest.dreamchaser.category.Category;
import com.boardtest.dreamchaser.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // ★★★ 마이페이지용: 작성자를 기준으로 모든 "상태"의 글을 찾음 (수정 없음) ★★★
    List<Post> findByAuthor(User author);


    // ★★★ 카테고리별 목록 조회용: 특정 카테고리 & 특정 상태의 글만 페이징하여 찾음 ★★★
    Page<Post> findByCategoryAndStatus(Category category, PostStatus status, Pageable pageable);

    // ★★★ 검색용: 특정 상태의 글 중에서만 키워드로 검색 ★★★
    @Query("SELECT p FROM Post p WHERE p.status = :status AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%)")
    Page<Post> findByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") PostStatus status, Pageable pageable);

    // ★★★ 메인 페이지 최신글용: 특정 상태의 글 중 최신 6개를 찾음 ★★★
    List<Post> findTop6ByStatusOrderByCreateDateDesc(PostStatus status);

    // ★★★ 추천순 정렬용: 특정 상태의 글 중에서만 추천순으로 정렬 ★★★
    @Query("SELECT p FROM Post p WHERE p.status = :status ORDER BY SIZE(p.voter) DESC, p.createDate DESC")
    Page<Post> findAllByStatusOrderByVoterDesc(@Param("status") PostStatus status, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.status = :status ORDER BY SIZE(p.voter) ASC, p.createDate DESC")
    Page<Post> findAllByStatusOrderByVoterAsc(@Param("status") PostStatus status, Pageable pageable);

    // ★★★ 관리자 승인 대기 목록용: 특정 상태의 글만 찾음 (추후 사용) ★★★
    Page<Post> findByStatus(PostStatus status, Pageable pageable); // 위와 중복되지만, 명시적으로 재사용함을 알림

}