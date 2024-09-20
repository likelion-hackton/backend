package com.example.backend.common.jwt;

import com.example.backend.member.entity.Member;
import com.example.backend.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final String secretKey;

    Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = header.split(" ")[1];

            try {
                if (JwtTokenUtil.isExpired(token, secretKey)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "토큰이 만료되었습니다.");
                }
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다.");
            }

            String email = JwtTokenUtil.getEmail(token, secretKey);
            Member memberByEmail = memberService.getMemberByEmail(email);

            if (memberByEmail == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "토큰 사용자를 찾을 수 없습니다.");
            }

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + memberByEmail.getMemberInfo().getPermission());
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(authority);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    memberByEmail.getEmail(), null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        } catch (ResponseStatusException e) {
            logger.error("JWT 인증 실패: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            sendErrorResponse(response, e.getStatusCode().value(), e.getReason());
        } catch (Exception e) {
            logger.error("JWT 처리 중 예상치 못한 오류 발생: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String jsonResponse = String.format("{\"error\": \"%s\"}", message);
        response.getWriter().write(jsonResponse);
    }
}
