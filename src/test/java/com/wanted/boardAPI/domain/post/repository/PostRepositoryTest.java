package com.wanted.boardAPI.domain.post.repository;

import com.wanted.boardAPI.domain.member.entity.Member;
import com.wanted.boardAPI.domain.member.repository.MemberRepository;
import com.wanted.boardAPI.domain.post.entity.Post;
import com.wanted.boardAPI.domain.post.entity.request.CreatePostRequest;
import com.wanted.boardAPI.domain.post.entity.response.PostResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest

public class PostRepositoryTest {
    @Autowired
    protected PostRepository postRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @Test
    @DisplayName("findPostsConvertToDto")
    void findPostsConvertToDto() throws Exception {
        //given
        Member member = memberRepository.save(Member.builder() //"abcd@1234 계정으로 찾은 member
                .email("abcd@1234")
                .password("12345678")
                .build());

        //더미 데이터 생성
        for (int i = 0; i < 10; i++) {
            postRepository.save(Post.of(member, new CreatePostRequest("제목1", "내용1")));
        }

        //when
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> posts = postRepository.findPostsConvertToDto(pageable);

        //then
        Assertions.assertThat(posts.getSize()).isEqualTo(size);
        Assertions.assertThat(posts.getNumber()).isEqualTo(page);
    }
}
