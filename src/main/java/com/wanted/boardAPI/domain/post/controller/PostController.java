package com.wanted.boardAPI.domain.post.controller;

import com.wanted.boardAPI.base.security.CustomUser;
import com.wanted.boardAPI.domain.member.entity.request.JoinMemberRequest;
import com.wanted.boardAPI.domain.member.service.MemberService;
import com.wanted.boardAPI.domain.post.entity.Post;
import com.wanted.boardAPI.domain.post.entity.request.CreatePostRequest;
import com.wanted.boardAPI.domain.post.entity.request.EditPostRequest;
import com.wanted.boardAPI.domain.post.entity.response.PostResponse;
import com.wanted.boardAPI.domain.post.service.PostService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Post> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreatePostRequest createPostRequest) {
        Post post = postService.create(user.getUsername(), createPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> findAll(Pageable pageable) {
        Page<PostResponse> posts = postService.findPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("{postId}")
    public ResponseEntity<PostResponse> findOne(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.findPostOne(postId));
    }

    @PatchMapping("{postId}")
    public ResponseEntity<PostResponse> edit(
            @AuthenticationPrincipal User user,
            @PathVariable Long postId,
            @Valid @RequestBody EditPostRequest editPostRequest) {
        return ResponseEntity.ok(postService.edit(user.getUsername(), postId, editPostRequest));
    }


    @DeleteMapping("{postId}")
    public ResponseEntity<Void> edit(
            @AuthenticationPrincipal User user,
            @PathVariable Long postId) {
        postService.delete(user.getUsername(), postId);
        return ResponseEntity.noContent().build();
    }
}
