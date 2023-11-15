package com.specificgroup.gateway.config;

import com.specificgroup.gateway.auth.CustomAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final CustomAuthenticationManager authenticationManager;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http.cors().and().csrf()
                .disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/users/**")
                .permitAll()
                .pathMatchers(HttpMethod.GET, "/posts/**")
                .permitAll()
                .pathMatchers("/users/**", "/posts/**")
                .permitAll()
//                .authenticated()
                .and().authenticationManager(authenticationManager)
                .authorizeExchange().anyExchange().permitAll().and()
                .httpBasic().and()
                .build();
    }
}