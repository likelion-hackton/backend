package com.example.backend.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // 서버 정보 설정 (로컬 서버 및 프로덕션 서버)
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local Server");

        Server prodServer = new Server()
                .url("http://sangsang2.kr:8080")
                .description("Production Server");

        // OpenAPI 구성
        return new OpenAPI()
                .info(new Info()
                        .title("멋사 교내해커톤 API")
                        .version("1.0")
                        .description("멋사 교내해커톤 API 명세서"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))  // 인증 요구 추가
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))  // JWT 인증 스키마 추가
                .servers(Arrays.asList(localServer, prodServer));  // 서버 목록 추가
    }

    @Bean
    public GroupedOpenApi api() {
        // Grouped API 설정
        return GroupedOpenApi.builder()
                .group("default")  // 그룹 이름 설정
                .pathsToMatch("/**")  // 모든 경로 매칭
                .addOpenApiCustomizer(openApiCustomizer())  // 커스터마이저 추가
                .build();
    }

    // 커스터마이저로 모든 API에 SecurityRequirement 추가
    private OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem ->
                    pathItem.readOperations().forEach(operation ->
                            operation.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))));
        };
    }
}
