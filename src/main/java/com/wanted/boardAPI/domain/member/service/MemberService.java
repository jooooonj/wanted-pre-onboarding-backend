package com.wanted.boardAPI.domain.member.service;

import com.wanted.boardAPI.domain.member.entity.Member;
import com.wanted.boardAPI.domain.member.entity.request.JoinMemberRequest;
import com.wanted.boardAPI.domain.member.entity.request.LoginMemberRequest;
import com.wanted.boardAPI.domain.member.repository.MemberRepository;
import com.wanted.boardAPI.global.jwt.JwtToken;
import com.wanted.boardAPI.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public Member findByEmail(String email){
        return memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException(email+"로 가입된 계정이 존재하지 않습니다.")
        );
    }

    @Transactional
    public Long join(JoinMemberRequest joinMemberRequest){
        if(checkDuplicateEmail(joinMemberRequest.getEmail()))
            throw new IllegalArgumentException("해당 이메일로 가입된 계정이 존재합니다.");

        Member member = Member.builder()
                .email(joinMemberRequest.getEmail())
                .password(passwordEncoder.encode(joinMemberRequest.getPassword()))
                .build();

        return memberRepository.save(member).getId();
    }

    public JwtToken login(LoginMemberRequest loginMemberRequest) {
        Member member = findByEmail(loginMemberRequest.getEmail());

        if (!passwordEncoder.matches(loginMemberRequest.getPassword(), member.getPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        return jwtTokenProvider.genToken(member.toClaims(), 60 * 60 * 1); //1시간
    }

    //이메일 중복 체크, 중복이면 true
    private boolean checkDuplicateEmail(String email) {
        return  memberRepository.findByEmail(email).isPresent();
    }
}
