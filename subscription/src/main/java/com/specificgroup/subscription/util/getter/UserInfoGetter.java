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

import static com.specificgroup.subscription.util.Constants.Message.RESOURCE_NOT_FOUND;
import static com.specificgroup.subscription.util.Constants.Message.UNAVAILABLE_SERVICE;
import static com.specificgroup.subscription.util.Constants.UrlPath.USER_EXISTENCE_CHECK_URL;

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
        String uri = getUserUrlFromEureka() + USER_EXISTENCE_CHECK_URL + userId;
        return webClient.get().uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> handleUserServiceError(response))
                .onStatus(HttpStatus::is5xxServerError, error -> Mono.error(
                        new ServiceUnavailableException(String.format(UNAVAILABLE_SERVICE, "User"))))
                .bodyToMono(Boolean.class)
                .block();
    }

    private String getUserUrlFromEureka() {
        try {
            return eurekaClient.getNextServerFromEureka("user", false).getHomePageUrl();
        } catch (RuntimeException e) {
            logger.error(String.format(UNAVAILABLE_SERVICE, "User"));
            throw new ServiceUnavailableException(String.format(UNAVAILABLE_SERVICE, "User"));
        }
    }

    private Mono<? extends Throwable> handleUserServiceError(ClientResponse response) {
        if (response.statusCode() == HttpStatus.NOT_FOUND) {
            logger.error(String.format(RESOURCE_NOT_FOUND, "User(s)"));
            return Mono.error(new ServiceClientException(String.format(RESOURCE_NOT_FOUND, "User(s)")));
        } else {
            return response.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new ServiceClientException("Bad request. Error: " + errorBody)));
        }
    }
}