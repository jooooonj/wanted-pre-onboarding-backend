package com.wanted.boardAPI.domain.member.entity.request;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class LoginMemberRequest {
    @Pattern(regexp = ".*@.*", message = "이메일은 '@'를 반드시 포함해야 합니다.")
    private String email;

    @Size(min = 8, message = "비밀번호는 8자 이상 입력해주세요.")
    private String password;
}
