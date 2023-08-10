package com.wanted.boardAPI.domain.post.service;

import com.wanted.boardAPI.domain.member.entity.Member;
import com.wanted.boardAPI.domain.member.service.MemberService;
import com.wanted.boardAPI.domain.post.entity.Post;
import com.wanted.boardAPI.domain.post.entity.request.CreatePostRequest;
import com.wanted.boardAPI.domain.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@Slf4j
class PostServiceTest {

    @InjectMocks
    protected PostService postService;

    @Mock
    protected PostRepository postRepository;

    @Mock
    protected MemberService memberService;

    @Test
    @DisplayName("create(게시글 작성)")
    void join1() throws Exception {
        //given
        CreatePostRequest request = CreatePostRequest.builder()
                .title("제목입니다.").content("내용입니다.").build();

        Member member = Member.builder()
                .email("abcd@1234").build();

        Post post = Post.of(member, request);

        given(memberService.findByEmail(anyString())).willReturn(member);
        given(postRepository.save(any(Post.class))).willReturn(post);

        //when
        Post savedPost = postService.create(member.getEmail(), request);

        //then
        log.debug(savedPost.getTitle());
        log.debug(savedPost.getContent());
        Assertions.assertThat(savedPost.getTitle()).isEqualTo(post.getTitle());
        Assertions.assertThat(savedPost.getContent()).isEqualTo(post.getContent());
    }
    
}