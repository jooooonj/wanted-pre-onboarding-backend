package com.wanted.boardAPI.domain.member.service;

import com.wanted.boardAPI.domain.member.entity.Member;
import com.wanted.boardAPI.domain.member.entity.request.JoinMemberRequest;
import com.wanted.boardAPI.domain.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

//통합테스트
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    protected MemberService memberService;

    @Mock
    protected MemberRepository memberRepository;

    @Mock
    protected BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 테스트 성공")
    void join1() throws Exception{
        //given
        JoinMemberRequest request = JoinMemberRequest.builder()
                .email("abcd@1234").password("123456789").build();
        Member member = mock(Member.class);
        given(member.getId()).willReturn(1L); //member의 id는 1로 설정
        given(memberRepository.findByEmail(request.getEmail())).willReturn(Optional.empty()); //중복이 없다면
        given(memberRepository.save(any(Member.class))).willReturn(member); //member 반환

        //when
        Long memberId = memberService.join(request);

        //then
        Assertions.assertThat(memberId).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("회원가입 테스트 실패 (중복 이메일)")
    void join2() throws Exception{
        //given
        JoinMemberRequest request = JoinMemberRequest.builder()
                .email("abcd@1234").password("123456789").build();
        Member member = mock(Member.class);
        given(memberRepository.findByEmail(request.getEmail())).willReturn(Optional.of(member)); //값이 있는 경우

        //when, then
        assertThrows(IllegalArgumentException.class, () -> {
            memberService.join(request);
        });
    }
}