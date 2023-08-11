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
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class PostRepositoryTest {
    @Autowired
    protected PostRepository postRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @Test
    @DisplayName("findPostsConvertToDto()")
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

    @Test
    @DisplayName("findOneConvertToDto()")
    void findOneConvertToDto() throws Exception {
        //given
        Member member = memberRepository.save(Member.builder() //"abcd@1234 계정으로 찾은 member
                .email("abcd@1234")
                .password("12345678")
                .build());
        Post post = postRepository.save(Post.of(member, new CreatePostRequest("제목1", "내용1")));

        //when
        Optional<PostResponse> _findPost = postRepository.findOneConvertToDto(post.getId());

        //then
        Assertions.assertThat(_findPost).isNotNull();
        PostResponse findPost = _findPost.get();
        Assertions.assertThat(findPost.getPostId()).isEqualTo(post.getId());
        Assertions.assertThat(findPost.getTitle()).isEqualTo(post.getTitle());
        Assertions.assertThat(findPost.getContent()).isEqualTo(post.getContent());
        Assertions.assertThat(member.getEmail()).isEqualTo(post.getMember().getEmail());
    }
}
