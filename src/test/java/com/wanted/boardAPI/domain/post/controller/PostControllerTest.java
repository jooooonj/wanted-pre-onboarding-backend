package com.wanted.boardAPI.domain.post.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.boardAPI.domain.member.controller.MemberController;
import com.wanted.boardAPI.domain.member.entity.Member;
import com.wanted.boardAPI.domain.member.entity.request.JoinMemberRequest;
import com.wanted.boardAPI.domain.member.repository.MemberRepository;
import com.wanted.boardAPI.domain.member.service.MemberService;
import com.wanted.boardAPI.domain.post.entity.Post;
import com.wanted.boardAPI.domain.post.entity.request.CreatePostRequest;
import com.wanted.boardAPI.domain.post.entity.request.EditPostRequest;
import com.wanted.boardAPI.domain.post.entity.response.PostResponse;
import com.wanted.boardAPI.domain.post.repository.PostRepository;
import com.wanted.boardAPI.domain.post.service.PostService;
import com.wanted.boardAPI.global.config.SecurityConfig;
import com.wanted.boardAPI.global.jwt.JwtAuthorizationFilter;
import com.wanted.boardAPI.global.jwt.JwtTokenProvider;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Slf4j
class PostControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected PostRepository postRepository;
    @MockBean
    protected PostService postService;


    @Test
    @DisplayName("게시글 작성 실패 - 제목 비어있음.")
    @WithMockUser(username = "abcd@1234")
    void create1() throws Exception {
        CreatePostRequest request = CreatePostRequest.builder()
                .title("").content("내용입니다.").build();

        MvcResult result = mockMvc.perform(
                        post("/api/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andReturn();// 400 Bad Request

        String message = result.getResolvedException().getMessage();
        log.debug("errorMessage : {} ", message);
        Assertions.assertThat(message).contains("제목은 필수값입니다.");
    }

    @Test
    @DisplayName("게시글 작성 실패 - 내용이 비어있음.")
    @WithMockUser(username = "abcd@1234")
    void create2() throws Exception {
        CreatePostRequest request = CreatePostRequest.builder()
                .title("제목입니다.").content("").build();

        MvcResult result = mockMvc.perform(
                        post("/api/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andReturn();// 400 Bad Request

        String message = result.getResolvedException().getMessage();
        log.debug("errorMessage : {} ", message);
        Assertions.assertThat(message).contains("내용은 필수값입니다.");
    }

    @Test
    @DisplayName("게시글 작성 성공")
    @WithMockUser(username = "abcd@1234")
    void create3() throws Exception {
        //given
        Member member = memberRepository.save(Member.builder() //"abcd@1234 계정으로 찾은 member
                .email("abcd@1234")
                .password("12345678")
                .build());
        memberRepository.save(member);
        CreatePostRequest request = CreatePostRequest.builder()
                .title("제목입니다.").content("내용입니다").build();

        //when
        MvcResult result = mockMvc.perform(
                        post("/api/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        //then
        verify(postService).create(eq("abcd@1234"), any(CreatePostRequest.class));
    }

    @Test
    @DisplayName("게시글 목록 조회")
    @WithMockUser(username = "abcd@1234")
    void findPosts() throws Exception {
        // given
        int page = 0;
        int size = 5;

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/posts")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        //then
        verify(postService).findPosts(any(Pageable.class));
    }

    @Test
    @DisplayName("게시글 단건 조회")
    @WithMockUser(username = "abcd@1234")
    void findOne() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(postService).findPostOne(eq(1L));
    }

    @Test
    @DisplayName("게시글 수정 요청")
    @WithMockUser(username = "abcd@1234")
    void edit() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder() //"abcd@1234 계정으로 찾은 member
                .email("abcd@1234")
                .password("12345678")
                .build());
        EditPostRequest request = EditPostRequest.builder()
                .title("수정한 제목")
                .content("수정한 내용")
                .build();

        //데이터 생성
        Post post = postRepository.save(Post.of(member, new CreatePostRequest("제목1", "내용1")));

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/posts/1")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(postService).edit(eq("abcd@1234"), eq(1L), any(EditPostRequest.class));
    }

    @Test
    @DisplayName("게시글 삭제 요청")
    @WithMockUser(username = "abcd@1234")
    void delete() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder() //"abcd@1234 계정으로 찾은 member
                .email("abcd@1234")
                .password("12345678")
                .build());
        EditPostRequest request = EditPostRequest.builder()
                .title("수정한 제목")
                .content("수정한 내용")
                .build();

        //데이터 생성
        Post post = postRepository.save(Post.of(member, new CreatePostRequest("제목1", "내용1")));

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(postService).delete(eq("abcd@1234"), eq(1L));
    }
}