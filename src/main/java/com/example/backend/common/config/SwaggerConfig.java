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
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local Server");

        Server prodServer = new Server()
                .url("http://sangsang2.kr:8080")
                .description("Production Server");

        return new OpenAPI()
                .info(new Info().title("멋사 교내해커톤 API").version("1.0").description("멋사 교내해커톤 API 명세서"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new io.swagger.v3.oas.models.security.SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .servers(Arrays.asList(localServer,prodServer));
    }

    @Bean
    public GroupedOpenApi api(){
        return GroupedOpenApi.builder()
                .group("")
                .pathsToMatch("/**")
                .addOpenApiCustomizer(openApiCustomizer())
                .build();
    }

    private OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                operation.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
            }));
        };
    }
}

