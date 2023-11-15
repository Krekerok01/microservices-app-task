package com.specificgroup.gateway.config;

import com.specificgroup.gateway.auth.CustomAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class WebSecurityConfig {
    private final CustomAuthenticationManager authenticationManager;

    @Autowired
    public WebSecurityConfig(CustomAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                .pathMatchers("/posts/**").permitAll()
                .pathMatchers("/users/**").authenticated()
                .and().authenticationManager(authenticationManager)
                .authorizeExchange().anyExchange().permitAll().and()
                .httpBasic().and()
                .build();
    }
}