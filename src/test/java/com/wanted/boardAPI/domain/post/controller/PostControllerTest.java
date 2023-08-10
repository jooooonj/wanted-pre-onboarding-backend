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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
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

    @Autowired
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
                .andReturn();

        //then
        String bodyValue = result.getResponse().getContentAsString();
        Assertions.assertThat(bodyValue).isNotNull();
    }

    @Test
    @DisplayName("게시글 목록 조회")
    @WithMockUser(username = "abcd@1234")
    void findPosts() throws Exception {
        // given
        // Page<PostResponse> 생성
        List<PostResponse> dummyPosts = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            dummyPosts.add(new PostResponse("user", Long.valueOf(i), "제목" + i, "내용" + i));
        }
        Page<PostResponse> posts = new PageImpl<>(dummyPosts);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/posts")
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("게시글 단건 조회")
    @WithMockUser(username = "abcd@1234")
    void findOne() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder() //"abcd@1234 계정으로 찾은 member
                .email("abcd@1234")
                .password("12345678")
                .build());

        //데이터 생성
        postRepository.save(Post.of(member, new CreatePostRequest("제목1", "내용1")));

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}