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

import java.util.List;

@Component
@RequiredArgsConstructor
public class InfoGetter {

    private final EurekaClient eurekaClient;
    private final WebClient webClient;

    public UserInfoResponse getUserById(Long userId) {
        String uri = getUserUrlFromEureka() + "/users/" + userId + "/username";
        return webClient.get().uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> handleServiceError(response))
                .onStatus(HttpStatus::is5xxServerError, error -> Mono.error(new ServiceUnavailableException("User service is unavailable. Try again later.")))
                .bodyToMono(UserInfoResponse.class)
                .block();
    }

    public String getUsernameByUserId(Long userId){
        UserInfoResponse userInfoResponse = getUserById(userId);
        return userInfoResponse.getUsername();
    }

    public List<Long> getSubscriptionIdsListBySubscriberId(Long requestUserId) {
        String uri = getSubscriptionUrlFromEureka() + "/api/v1/subscriptions/subscriber?userSubscriberId=" + requestUserId;
        return webClient.get().uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> handleServiceError(response))
                .onStatus(HttpStatus::is5xxServerError, error -> Mono.error(new ServiceUnavailableException("Subscription service is unavailable. Try again later.")))
                .bodyToFlux(Long.class)
                .collectList()
                .block();
    }

    private String getUserUrlFromEureka() {
        try {
            return eurekaClient.getNextServerFromEureka("user", false).getHomePageUrl();
        } catch (RuntimeException e) {
            throw new ServiceUnavailableException("User service is unavailable. Try again later.");
        }
    }

    private String getSubscriptionUrlFromEureka() {
        try {
            return eurekaClient.getNextServerFromEureka("subscription-service", false).getHomePageUrl();
        } catch (RuntimeException e) {
            throw new ServiceUnavailableException("Subscription service is unavailable. Try again later.");
        }
    }

    private Mono<? extends Throwable> handleServiceError(ClientResponse response) {
        if (response.statusCode() == HttpStatus.NOT_FOUND) {
            return Mono.error(new ServiceClientException("Page not found."));
        } else {
            return response.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new ServiceClientException("Bad request. Error: " + errorBody)));
        }
    }
}