package com.example.backend.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // cors 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 적용 url 패턴
                .allowedOrigins("http://localhost:3000", "https://momentclass.com") // 허용된 Origin
                .allowedMethods("GET", "POST", "PATCH", "DELETE") // 허용된 Http Methods
                .allowedHeaders("*") // 허용된 Header
                .exposedHeaders("Authorization") // 허용된 Header 값
                .allowCredentials(true) // 쿠키 인증 요청
                .maxAge(3600); // request 캐싱 시간
    }
}
