package com.wanted.boardAPI.domain.post.repository;

import com.wanted.boardAPI.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
