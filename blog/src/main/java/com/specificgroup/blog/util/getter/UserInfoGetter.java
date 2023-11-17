package com.specificgroup.blog.util.getter;

import com.netflix.discovery.EurekaClient;
import com.specificgroup.blog.dto.user_service.UserInfoResponse;
import com.specificgroup.blog.exception.ServiceClientException;
import com.specificgroup.blog.exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserInfoGetter {

    private final EurekaClient eurekaClient;
    private final WebClient webClient;

    public UserInfoResponse getUserById(Long userId) {
        String uri = getUserUrlFromEureka() + "/users/" + userId;
        return webClient.get().uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> handleUserServiceError(response))
                .onStatus(HttpStatus::is5xxServerError, error -> Mono.error(new ServiceUnavailableException("User service is unavailable. Try again later.")))
                .bodyToMono(UserInfoResponse.class)
                .block();
    }

    public String getUsernameByUserId(Long userId){
        UserInfoResponse userInfoResponse = getUserById(userId);
        return userInfoResponse.getUsername();
    }

    private String getUserUrlFromEureka() {
        try {
            return eurekaClient.getNextServerFromEureka("user", false).getHomePageUrl();
        } catch (RuntimeException e) {
            throw new ServiceUnavailableException("User service is unavailable. Try again later.");
        }
    }

    private Mono<? extends Throwable> handleUserServiceError(ClientResponse response) {
        if (response.statusCode() == HttpStatus.NOT_FOUND) {
            return Mono.error(new ServiceClientException("User(s) not found."));
        } else {
            return response.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new ServiceClientException("Bad request. Error: " + errorBody)));
        }
    }
}
