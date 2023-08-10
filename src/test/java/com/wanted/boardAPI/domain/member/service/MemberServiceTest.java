package com.wanted.boardAPI.domain.member.service;

import com.wanted.boardAPI.domain.member.entity.Member;
import com.wanted.boardAPI.domain.member.entity.request.JoinMemberRequest;
import com.wanted.boardAPI.domain.member.entity.request.LoginMemberRequest;
import com.wanted.boardAPI.domain.member.repository.MemberRepository;
import com.wanted.boardAPI.global.jwt.JwtToken;
import com.wanted.boardAPI.global.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

//단위테스트
@ExtendWith(MockitoExtension.class)
@Slf4j
class MemberServiceTest {

    @InjectMocks
    protected MemberService memberService;

    @Mock
    protected MemberRepository memberRepository;

    @Mock
    protected BCryptPasswordEncoder passwordEncoder;
    @Mock
    protected JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("join(회원가입) 성공")
    void join1() throws Exception {
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
    @DisplayName("join(회원가입) 실패 - (중복 이메일)")
    void join2() throws Exception {
        //given
        JoinMemberRequest request = JoinMemberRequest.builder()
                .email("abcd@1234").password("123456789").build();
        Member member = mock(Member.class);
        given(memberRepository.findByEmail(request.getEmail())).willReturn(Optional.of(member)); //값이 있는 경우

        //when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            memberService.join(request);
        });

        //then
        log.debug("errorMessage : {}", e.getMessage());
        Assertions.assertThat(e.getMessage()).isEqualTo("해당 이메일로 가입된 계정이 존재합니다.");
    }

    @Test
    @DisplayName("login(로그인) 실패 - (계정이 존재하지 않는다.)")
    void login1() throws Exception {
        //given
        LoginMemberRequest request = LoginMemberRequest.builder()
                .email("abcd@1234").password("123456789").build();
        Member member = mock(Member.class);
        given(memberRepository.findByEmail(request.getEmail())).willReturn(Optional.empty()); //값이 없는 경우

        //when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            memberService.login(request);
        });

        //then
        log.debug("errorMessage : , {} ", e.getMessage());
        Assertions.assertThat(e.getMessage()).isEqualTo(request.getEmail() + "로 가입된 계정이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("login(로그인) 실패 - (비밀번호가 일치하지 않는다.)")
    void login2() throws Exception {
        //given
        //request와 member의 패스워드가 틀리다.
        LoginMemberRequest request = LoginMemberRequest.builder()
                .email("abcd@1234").password("87654321").build();

        Member member = Member.builder() //"abcd@1234 계정으로 찾은 member
                .email("abcd@1234")
                .password("12345678")
                .build();

        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member)); //값이 있는 경우

        //주어진 request와 member의 패스워드가 같으면 true, 틀리면 false로 실제 동작을 반영
        if (member.getPassword().equals(request.getPassword()))
            given(passwordEncoder.matches(request.getPassword(), member.getPassword())).willReturn(true);
        else
            given(passwordEncoder.matches(request.getPassword(), member.getPassword())).willReturn(false);

        //when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            memberService.login(request);
        });

        //then
        log.debug("errorMessage : , {} ", e.getMessage());
        Assertions.assertThat(e.getMessage()).isEqualTo("비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("login(로그인) 성공")
    void login3() throws Exception {
        //given
        //request와 member의 정보가 일치한다.
        JwtToken token = JwtToken.builder()
                .accessToken("accessToken_test")
                .refreshToken("refreshToken_test")
                .build();

        Member member = mock(Member.class);
        Map memberToMap = Map.of(1L, "abcd@1234"); //member 정보

        LoginMemberRequest request = LoginMemberRequest.builder()
                .email("abcd@1234").password("12345678").build();

        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));

        //member의 패스워드와 request의 패스워드가 일치한다는 가정
        given(passwordEncoder.matches(request.getPassword(), member.getPassword())).willReturn(true);
        given(jwtTokenProvider.genToken(anyMap(), anyInt())).willReturn(token);
        given(member.toClaims()).willReturn(memberToMap);

        //when
        JwtToken jwtToken = memberService.login(request);

        //then
        verify(jwtTokenProvider).genToken(anyMap(), anyInt()); //해당 메서드가 실행되는지. 실행된다면 성공적으로 수행된 것임
        Assertions.assertThat(jwtToken.getAccessToken()).isNotNull();
        Assertions.assertThat(jwtToken.getRefreshToken()).isNotNull();
    }
}