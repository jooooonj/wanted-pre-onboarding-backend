package com.wanted.boardAPI.global.jwt;

import com.wanted.boardAPI.base.security.CustomUser;
import com.wanted.boardAPI.domain.member.entity.Member;
import com.wanted.boardAPI.domain.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 Authorization 값을 가져온다.
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null) {
            String token = bearerToken.substring("Bearer ".length());

            if (jwtTokenProvider.verify(token)) { //token이 유효하면
                Map<String, Object> claims = jwtTokenProvider.getClaimsToMap(token);
                String email = (String) claims.get("email");

                Member member = memberService.findByEmail(email);
                forceAuthentication(member);
            }
        }

        filterChain.doFilter(request, response);
    }

    // 강제로 로그인 처리하는 메소드
    private void forceAuthentication(Member member) {
        User user = new User(member.getEmail(), member.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("DEFAULT")));

        // 스프링 시큐리티 객체에 저장할 authentication 객체를 생성
        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        user,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("DEFAULT"))
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
