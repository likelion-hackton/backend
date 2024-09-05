package com.example.backend.common.security;

import com.example.backend.common.jwt.JwtTokenFilter;
import com.example.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberService memberService;
    @Value("${spring.jwt.secretKey}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions().disable()) // h2 db 접근용
                .csrf(AbstractHttpConfigurer::disable) // csrf 끔
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 기본 필터 실행 전 JwtTokenFilter 먼저 실행
                .addFilterBefore(new JwtTokenFilter(memberService, secretKey), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("**").permitAll()
                        .anyRequest().authenticated()) // 일단 모든 요청에 권한을 확인하지 않음
                .build();
    }
}
