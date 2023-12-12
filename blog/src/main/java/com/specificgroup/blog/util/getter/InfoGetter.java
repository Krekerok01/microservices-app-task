package com.specificgroup.blog.util.getter;

import com.netflix.discovery.EurekaClient;
import com.specificgroup.blog.dto.user_service.UserInfoResponse;
import com.specificgroup.blog.exception.ServiceClientException;
import com.specificgroup.blog.exception.ServiceUnavailableException;
import com.specificgroup.blog.util.logger.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.specificgroup.blog.util.Constants.Message.RESOURCE_NOT_FOUND;
import static com.specificgroup.blog.util.Constants.Message.UNAVAILABLE_SERVICE;
import static com.specificgroup.blog.util.Constants.UrlPath.GET_SUBSCRIPTIONS_IDS_URL;

/**
 * Util class for getting information from third-party services
 */
@Component
@RequiredArgsConstructor
public class InfoGetter {

    private final EurekaClient eurekaClient;
    private final WebClient webClient;
    private final Logger logger;

    /**
     * Get username for a user by user id
     *
     * @return a username of the user
     */
    public String getUsernameByUserId(Long userId){
        String uri = getUserUrlFromEureka() + "/users/" + userId + "/username";
        UserInfoResponse userInfoResponse = webClient.get().uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> handleServiceError(response))
                .onStatus(HttpStatus::is5xxServerError, error -> Mono.error(new
                        ServiceUnavailableException(String.format(UNAVAILABLE_SERVICE, "User"))))
                .bodyToMono(UserInfoResponse.class)
                .block();

        return userInfoResponse.getUsername();
    }

    /**
     * Get list of subscription ids for a user by user id
     *
     * @return a list of subscription ids
     */
    public List<Long> getSubscriptionIdsListBySubscriberId(Long requestUserId) {
        String uri = getSubscriptionUrlFromEureka() + GET_SUBSCRIPTIONS_IDS_URL + requestUserId;
        return webClient.get().uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> handleServiceError(response))
                .onStatus(HttpStatus::is5xxServerError, error -> Mono.error(new
                        ServiceUnavailableException(String.format(UNAVAILABLE_SERVICE, "Subscription"))))
                .bodyToFlux(Long.class)
                .collectList()
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

    private String getSubscriptionUrlFromEureka() {
        try {
            return eurekaClient.getNextServerFromEureka("subscription-service", false).getHomePageUrl();
        } catch (RuntimeException e) {
            logger.error(String.format(UNAVAILABLE_SERVICE, "Subscription"));
            throw new ServiceUnavailableException(String.format(UNAVAILABLE_SERVICE, "Subscription"));
        }
    }

    private Mono<? extends Throwable> handleServiceError(ClientResponse response) {
        if (response.statusCode() == HttpStatus.NOT_FOUND) {
            logger.error(String.format(RESOURCE_NOT_FOUND, "Page"));
            return Mono.error(new ServiceClientException(String.format(RESOURCE_NOT_FOUND, "Page")));
        } else {
            return response.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new ServiceClientException("Bad request. Error: " + errorBody)));
        }
    }
}