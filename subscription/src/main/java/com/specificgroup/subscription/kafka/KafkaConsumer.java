package com.specificgroup.subscription.kafka;


import com.specificgroup.subscription.dto.UserServiceMessage;
import com.specificgroup.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Provides methods for listening Kafka topics, receiving data and sending for further processing.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final SubscriptionService subscriptionService;

    @KafkaListener(topics = "${spring.kafka.topics.user.service.request}")
    public void consumeUserPostsDeleting(UserServiceMessage message)  {
        log.info("Received a request to delete subscriptions for user {}", message);
        subscriptionService.deleteSubscriptionsByUserId(message);
    }
}