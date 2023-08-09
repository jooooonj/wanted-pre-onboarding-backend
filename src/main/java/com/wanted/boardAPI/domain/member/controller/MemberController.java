package com.wanted.boardAPI.domain.member.controller;


import com.wanted.boardAPI.domain.member.entity.request.JoinMemberRequest;
import com.wanted.boardAPI.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@RestController
public class MemberController {
    private final MemberService memberService;
    @PostMapping
    public ResponseEntity<Long> join(@Valid @RequestBody JoinMemberRequest joinMemberRequest) {
        Long memberId = memberService.join(joinMemberRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }
}
