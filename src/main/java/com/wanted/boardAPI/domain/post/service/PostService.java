package com.wanted.boardAPI.domain.post.service;

import com.wanted.boardAPI.domain.member.entity.Member;
import com.wanted.boardAPI.domain.member.service.MemberService;
import com.wanted.boardAPI.domain.post.entity.Post;
import com.wanted.boardAPI.domain.post.entity.request.CreatePostRequest;
import com.wanted.boardAPI.domain.post.entity.request.EditPostRequest;
import com.wanted.boardAPI.domain.post.entity.response.PostResponse;
import com.wanted.boardAPI.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public Page<PostResponse> findPosts(Pageable pageable) {
        return postRepository.findPostsConvertToDto(pageable);
    }

    public PostResponse findPostOne(Long postId) {
        return postRepository.findOneConvertToDto(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
    }

    public Post findById(Long postId){
        return postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
    }

    @Transactional
    public PostResponse edit(String email, Long postId, EditPostRequest editPostRequest) {
        Post post = findById(postId);

        if(!haveAccessPermission(post, email))
            throw new IllegalArgumentException("해당 게시글에 대한 접근 권한이 없습니다.");

        post.update(editPostRequest);
        return PostResponse.of(post);
    }

    private boolean haveAccessPermission(Post post, String email){
        return post.getMember().getEmail().equals(email);
    }
}
