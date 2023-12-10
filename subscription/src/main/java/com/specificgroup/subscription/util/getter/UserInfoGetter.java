package com.specificgroup.subscription.util.getter;

import com.netflix.discovery.EurekaClient;
import com.specificgroup.subscription.exception.ServiceClientException;
import com.specificgroup.subscription.exception.ServiceUnavailableException;
import com.specificgroup.subscription.util.logger.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Util class for getting information from third-party services
 */
@Component
@RequiredArgsConstructor
public class UserInfoGetter {

    private final EurekaClient eurekaClient;
    private final WebClient webClient;
    private final Logger logger;

    /**
     * Check for a user existence
     *
     * @return a boolean variable with information about the user's existence
     */
    public Boolean existsUserById(Long userId) {
        String uri = getUserUrlFromEureka() + "/users/exists/" + userId;
        return webClient.get().uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> handleUserServiceError(response))
                .onStatus(HttpStatus::is5xxServerError, error -> Mono.error(new ServiceUnavailableException("User service is unavailable. Try again later.")))
                .bodyToMono(Boolean.class)
                .block();
    }

    private String getUserUrlFromEureka() {
        try {
            return eurekaClient.getNextServerFromEureka("user", false).getHomePageUrl();
        } catch (RuntimeException e) {
            logger.error("User service is unavailable. Try again later.");
            throw new ServiceUnavailableException("User service is unavailable. Try again later.");
        }
    }

    private Mono<? extends Throwable> handleUserServiceError(ClientResponse response) {
        if (response.statusCode() == HttpStatus.NOT_FOUND) {
            logger.error("User(s) not found.");
            return Mono.error(new ServiceClientException("User(s) not found."));
        } else {
            return response.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new ServiceClientException("Bad request. Error: " + errorBody)));
        }
    }
}