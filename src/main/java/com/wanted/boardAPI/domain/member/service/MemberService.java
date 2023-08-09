package com.wanted.boardAPI.domain.member.service;

import com.wanted.boardAPI.domain.member.entity.Member;
import com.wanted.boardAPI.domain.member.entity.request.JoinMemberRequest;
import com.wanted.boardAPI.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Long join(JoinMemberRequest joinMemberRequest){
        if(checkDuplicateEmail(joinMemberRequest.getEmail()))
            throw new IllegalArgumentException("해당 이메일로 가입된 계정이 존재합니다.");

        Member member = Member.builder()
                .email(joinMemberRequest.getEmail())
                .password(passwordEncoder.encode(joinMemberRequest.getPassword()))
                .build();

        return memberRepository.save(member).getId();
    }


    //이메일 중복 체크, 중복이면 true
    private boolean checkDuplicateEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }
}
