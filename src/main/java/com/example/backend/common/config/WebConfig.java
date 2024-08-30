package com.example.backend.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 적용 url 패턴
                .allowedOrigins("*") // 허용된 Origin
                .allowedMethods("*") // 허용된 Http Methods
                .allowedHeaders("*") // 허용된 Header
                .exposedHeaders("*") // 허용된 Header 값
                .allowCredentials(false) // 쿠키 인증 요청
                .maxAge(3600); // request 캐싱 시간
    }
}
