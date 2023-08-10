package com.wanted.boardAPI.domain.post.service;

import com.wanted.boardAPI.domain.member.entity.Member;
import com.wanted.boardAPI.domain.member.service.MemberService;
import com.wanted.boardAPI.domain.post.entity.Post;
import com.wanted.boardAPI.domain.post.entity.request.CreatePostRequest;
import com.wanted.boardAPI.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    
    private final MemberService memberService;

    @Transactional
    public Post create(String email, CreatePostRequest createPostRequest) {
        Post post = Post.of(memberService.findByEmail(email), createPostRequest);
        return postRepository.save(post);
    }
}
