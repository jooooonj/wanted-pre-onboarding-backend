package com.wanted.boardAPI.domain.post.service;

import com.wanted.boardAPI.domain.member.entity.Member;
import com.wanted.boardAPI.domain.member.service.MemberService;
import com.wanted.boardAPI.domain.post.entity.Post;
import com.wanted.boardAPI.domain.post.entity.request.CreatePostRequest;
import com.wanted.boardAPI.domain.post.entity.request.EditPostRequest;
import com.wanted.boardAPI.domain.post.entity.response.PostResponse;
import com.wanted.boardAPI.domain.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

    @Test
    @DisplayName("findPosts(게시글 목록 조회)")
        //repository에게 위임
    void findPosts() throws Exception {
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);

        Page<PostResponse> posts = mock(Page.class);

        given(postRepository.findPostsConvertToDto(any())).willReturn(posts);
        postService.findPosts(pageable);

        verify(postRepository).findPostsConvertToDto(any(Pageable.class));
    }

    @Test
    @DisplayName("findPostOne(게시글 단건 조회)")
        //repository에게 위임
    void findPostOne() throws Exception {
        //given
        PostResponse post = mock(PostResponse.class);
        given(postRepository.findOneConvertToDto(anyLong())).willReturn(Optional.of(post));

        //when
        Long postId = 1L;
        PostResponse savedPost = postService.findPostOne(postId);

        verify(postRepository).findOneConvertToDto(postId);
    }

    @Test
    @DisplayName("edit(게시글 수정) 실패 - 작성자 본인이 아닌 경우")
    void edit1() throws Exception {
        //given
        Member member1 = Member.builder() //"abcd@1234 계정으로 찾은 member
                .email("abcd@1234")
                .password("12345678")
                .build();
        Member member2 = Member.builder() //"abcd@1234 계정으로 찾은 member
                .email("defg@5678")
                .password("12345678")
                .build();
        Post post = Post.of(member2, new CreatePostRequest("기존 제목", "기존 내용"));
        EditPostRequest request = EditPostRequest.builder()
                .content("새로운 내용")
                .title("새로운 제목")
                .build();
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        //when
        Long postId = 1L;
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            postService.edit(member1.getEmail(), postId, request);
        });

        //then
        log.debug("errorMessage : {}", e.getMessage());
        Assertions.assertThat(e.getMessage()).isEqualTo("해당 게시글에 대한 접근 권한이 없습니다.");
    }

    @Test
    @DisplayName("edit(게시글 수정) 성공")
    void edit2() throws Exception {
        //given
        Member member1 = Member.builder() //"abcd@1234 계정으로 찾은 member
                .email("abcd@1234")
                .password("12345678")
                .build();

        Post post = Post.of(member1, new CreatePostRequest("기존 제목", "기존 내용"));
        EditPostRequest request = EditPostRequest.builder()
                .content("새로운 내용")
                .title("새로운 제목")
                .build();
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        //when
        Long postId = 1L;
        PostResponse response = postService.edit(member1.getEmail(), postId, request);

        //then
        Assertions.assertThat(post.getContent()).isEqualTo("새로운 내용");
        Assertions.assertThat(post.getTitle()).isEqualTo("새로운 제목");
        Assertions.assertThat(response.getContent()).isEqualTo("새로운 내용");
        Assertions.assertThat(response.getTitle()).isEqualTo("새로운 제목");
    }
}