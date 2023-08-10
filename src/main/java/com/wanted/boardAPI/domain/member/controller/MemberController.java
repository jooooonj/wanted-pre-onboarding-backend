package com.wanted.boardAPI.domain.member.controller;


import com.wanted.boardAPI.domain.member.entity.request.JoinMemberRequest;
import com.wanted.boardAPI.domain.member.entity.request.LoginMemberRequest;
import com.wanted.boardAPI.domain.member.service.MemberService;
import com.wanted.boardAPI.global.jwt.JwtToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/join")
    public ResponseEntity<Long> join(@Valid @RequestBody JoinMemberRequest joinMemberRequest) {
        Long memberId = memberService.join(joinMemberRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@Valid @RequestBody LoginMemberRequest loginMemberRequest, HttpServletResponse response) {
        JwtToken jwtToken = memberService.login(loginMemberRequest);
        response.addHeader("Authentication", jwtToken.getAccessToken());

        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }
}
