package com.example.backend.common.jwt;

import com.example.backend.member.entity.Member;
import com.example.backend.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final String secretKey;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청에서 인증 정보 추출
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 헤더가 비었거나 Jwt 형식이 아닐 때 X
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰만 추출
        String token = header.split(" ")[1];

        // 만료시간 확인
        if (JwtTokenUtil.isExpired(token, secretKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = JwtTokenUtil.getEmail(token, secretKey);

        Member memberByEmail = memberService.getMemberByEmail(email);

        // 이메일에 유저가 존재하지 않으면 X
        if (memberByEmail == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 권한 추출
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + memberByEmail.getRole());
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(authority);

        // 토큰 설정
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                memberByEmail.getEmail(), null, authorities);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 저장
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
