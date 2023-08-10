package com.wanted.boardAPI.global.jwt;

import com.wanted.boardAPI.global.util.Ut;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.*;

@Component
@Primary
public class JwtTokenProvider {
    private SecretKey cachedSecretKey;

    @Value("${jwt.secretKey}")
    private String secretKeyPlain;

    private SecretKey _getSecretKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    public SecretKey getSecretKey() {
        if (cachedSecretKey == null) cachedSecretKey = _getSecretKey();

        return cachedSecretKey;
    }

    public JwtToken genToken(Map<String, Object> claims, int seconds) { //저장할 정보와, 토큰 유효기간을 설정
        long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + 1000L * seconds); //초단위로 설정할 수 있게끔 ex) 60 * 60 * 5 -> 5시간
        Date refreshTokenExpiresIn = new Date(now + 1000L * seconds * 10); //엑세스 토큰 10배
        String accessToken = Jwts.builder()
                .claim("body", Ut.json.toStr(claims))
                .setExpiration(accessTokenExpiresIn)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean verify(String token) { //토큰이 유효한지 확인
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public Map<String, Object> getClaimsToMap(String accessToken) {
        String body = getClaims(accessToken)
                .get("body", String.class);

        return Ut.json.toMap(body);
    }

    public Claims getClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
