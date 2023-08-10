package com.wanted.boardAPI.domain.post.repository;

import com.wanted.boardAPI.domain.post.entity.Post;
import com.wanted.boardAPI.domain.post.entity.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select new com.wanted.boardAPI.domain.post.entity.response.PostResponse(m.email, p.id, p.title, p.content) " +
            "from Post p join p.member m")
    Page<PostResponse> findPostsConvertToDto(Pageable pageable);
}
