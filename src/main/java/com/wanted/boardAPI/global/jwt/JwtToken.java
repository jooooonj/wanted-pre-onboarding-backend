package com.wanted.boardAPI.global.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {
    private String type;
    private String accessToken;
    private String refreshToken;
}
