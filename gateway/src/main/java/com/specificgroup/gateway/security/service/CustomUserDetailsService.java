package com.specificgroup.gateway.security.service;

import com.netflix.discovery.EurekaClient;
import com.specificgroup.gateway.security.JwtUser;
import com.specificgroup.gateway.dto.UserAuthDto;
import com.specificgroup.gateway.exception.ServiceClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.support.ServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Provides method for getting the user to further security checks
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final EurekaClient eurekaClient;
    private final WebClient webClient;
    private final String AUTH_URL = "users/auth";

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        String uri = String.format("%s%s/%s", getUsersUrlFromEureka(), AUTH_URL, username);
        return webClient.get().uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, this::handleTicketServiceError)
                .onStatus(HttpStatus::is5xxServerError, error -> Mono.error(new ServiceUnavailableException("Service is unavailable.")))
                .bodyToMono(UserAuthDto.class)
                .flatMap(user -> Mono.just(
                                JwtUser.builder()
                                        .role(user.getRole().toString())
                                        .username(user.getEmail())
                                        .build()
                        )
                );
    }

    private Mono<? extends Throwable> handleTicketServiceError(ClientResponse response) {
        if (response.statusCode() == HttpStatus.NOT_FOUND) {
            return Mono.error(new ServiceClientException("Recourse not found."));
        } else {
            return response.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(
                            new ServiceClientException("Bad request. Error: " + errorBody)));
        }
    }

    private String getUsersUrlFromEureka() {
        try {
            return eurekaClient
                    .getNextServerFromEureka("user", false)
                    .getHomePageUrl();
        } catch (RuntimeException e) {
            throw new RuntimeException("User service is unavailable. Try again later.");
        }
    }
}
