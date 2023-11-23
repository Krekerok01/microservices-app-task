package com.specificgroup.gateway.config;

import com.specificgroup.gateway.auth.CustomAuthenticationManager;
import com.specificgroup.gateway.auth.CustomSecurityContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;


@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {
    private final CustomAuthenticationManager authenticationManager;
    private final CustomSecurityContextRepository customSecurityContextRepository;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http.csrf()
                .disable()
                .cors()
                .configurationSource(corsConfiguration())
                .and()
                .authorizeExchange()
                .pathMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/webjars/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/users/**")
                .permitAll()
                .pathMatchers(HttpMethod.GET, "/posts/**", "/news/**", "/jobs/**")
                .permitAll()
                .pathMatchers(HttpMethod.GET, "/subscriptions")
                .hasAuthority("ADMIN")
                .pathMatchers("/posts/**", "/subscriptions/**", "/users/**")
                .authenticated()
                .and()
                .authenticationManager(authenticationManager)
                .securityContextRepository(customSecurityContextRepository)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.setAllowedOrigins(List.of("*"));
        corsConfig.setAllowedMethods(Arrays.asList("*"));
        corsConfig.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}