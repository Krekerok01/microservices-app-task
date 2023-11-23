package com.specificgroup.user.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.List;

@Configuration
@SecurityScheme(
        description = "Generate your JWT when you log in",
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SpringFoxConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("users")
                .packagesToScan("com.specificgroup.user.controller")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        OpenAPI info = new OpenAPI()
                .servers(List.of(new Server().url("http://localhost:8080")))
                .info(new Info().title("cloud API")
                        .description("cloud API")
                        .version("1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
        return info;
    }
}
