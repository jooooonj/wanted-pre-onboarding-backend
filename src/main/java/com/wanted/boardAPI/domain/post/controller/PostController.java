package com.wanted.boardAPI.domain.post.controller;

import com.wanted.boardAPI.base.security.CustomUser;
import com.wanted.boardAPI.domain.member.entity.request.JoinMemberRequest;
import com.wanted.boardAPI.domain.member.service.MemberService;
import com.wanted.boardAPI.domain.post.entity.Post;
import com.wanted.boardAPI.domain.post.entity.request.CreatePostRequest;
import com.wanted.boardAPI.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostController {
    private final PostService postService;


    @PostMapping()
    public ResponseEntity<Long> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreatePostRequest createPostRequest) {
        Post post = postService.create(user.getUsername(), createPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(post.getId());
    }
}
