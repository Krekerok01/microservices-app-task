package com.specificgroup.gateway.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CustomAuthenticationManager implements ReactiveAuthenticationManager {
    private final RestTemplate restTemplate;
    private final String JSON_AUTH_BODY = """
            {
            "username": "",
            "password": "%s",
            "email":"%s",
            "role":"DEFAULT"
            }""";
    private final String AUTH_URL = "http://localhost:8081/users/auth";

    @Autowired
    public CustomAuthenticationManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        log.info("User tried to auth with\nEmail: {}\nPassword: {}/n", name, password);

        String json = JSON_AUTH_BODY.formatted(password, name);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token;

        try {
            token = restTemplate.postForObject(AUTH_URL, new HttpEntity<>(json, headers), String.class);
        } catch (HttpClientErrorException ex) {
            throw new BadCredentialsException("1000");
        }
        if (token != null && !token.isEmpty()) {
            log.info("Generated token for user:{}", token);
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
            return Mono.just(new UsernamePasswordAuthenticationToken(name, password, grantedAuths));
        } else {
            return Mono.empty();
        }
    }
}
