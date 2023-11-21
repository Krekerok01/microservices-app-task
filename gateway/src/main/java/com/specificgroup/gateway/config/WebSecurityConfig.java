package com.specificgroup.gateway.config;

import com.specificgroup.gateway.auth.CustomAuthenticationManager;
import com.specificgroup.gateway.auth.CustomSecurityContextRepository;
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
    private final CustomSecurityContextRepository customSecurityContextRepository;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http.csrf()
                .disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/users/**")
                .permitAll()
                .pathMatchers(HttpMethod.GET, "/posts/**")
                .permitAll()
                .pathMatchers(HttpMethod.GET, "/subscriptions")
                .hasAuthority("ADMIN")
                .pathMatchers("/posts/**", "/subscriptions/**")
                .authenticated()
                .and()
                .authenticationManager(authenticationManager)
                .securityContextRepository(customSecurityContextRepository)
                .build();
    }
}