package com.specificgroup.subscription.service.impl;

import com.specificgroup.subscription.dto.SubscriptionServiceResponseMessage;
import com.specificgroup.subscription.dto.UserServiceMessage;
import com.specificgroup.subscription.entity.Subscription;
import com.specificgroup.subscription.exception.AccessDeniedException;
import com.specificgroup.subscription.exception.EntityNotFoundException;
import com.specificgroup.subscription.kafka.KafkaProducer;
import com.specificgroup.subscription.repository.SubscriptionRepository;
import com.specificgroup.subscription.service.SubscriptionService;
import com.specificgroup.subscription.util.getter.UserInfoGetter;
import com.specificgroup.subscription.util.logger.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{

    private final SubscriptionRepository subscriptionRepository;
    private final UserInfoGetter userInfoGetter;
    private final KafkaProducer kafkaProducer;
    private final Logger logger;
    @Value("${spring.kafka.topics.user.service.response.successful}")
    private String successfulResponseTopic;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Long createSubscription(Long userSubscriberId, Long userPublisherId) {
        logger.info("User with id=" + userSubscriberId +
                " subscribing to user with id=" + userPublisherId);

        checkThePossibilityOfCreatingASubscription(userSubscriberId, userPublisherId);

        Subscription subscription = Subscription.builder()
                .userSubscriberId(userSubscriberId)
                .userPublisherId(userPublisherId)
                .build();
        return subscriptionRepository.save(subscription)
                .getSubscriptionId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Subscription> getAllSubscriptions() {
        logger.info("Getting all subscriptions");
        return subscriptionRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> getSubscriptionsBySubscriberId(Long userSubscriberId) {
        logger.info("Getting subscriptions for user with id=" + userSubscriberId);
        return subscriptionRepository.findAllByUserSubscriberId(userSubscriberId)
                .stream()
                .map(Subscription::getUserPublisherId)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteSubscription(Long userPublisherId, Long userSubscriberId) {
        logger.info("User with id=" + userSubscriberId +
                " unsubscribing from the user with id=" + userPublisherId);

        Subscription subscription = subscriptionRepository
                .findByUserSubscriberIdAndUserPublisherId(userSubscriberId, userPublisherId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found"));
        if (!subscription.getUserSubscriberId().equals(userSubscriberId))
            throw new AccessDeniedException("Access denied");

        subscriptionRepository.delete(subscription);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteSubscriptionsByUserId(UserServiceMessage message) {
        logger.info("Deleting subscriptions with userId=" + message.getUserId());
        subscriptionRepository.deleteAllByUserSubscriberId(message.getUserId());

        SubscriptionServiceResponseMessage responseMessage = SubscriptionServiceResponseMessage.builder()
                .deletedUserId(message.getUserId())
                .message("Request successfully processed.")
                .build();
        kafkaProducer.notify(successfulResponseTopic, responseMessage);
    }

    private void checkThePossibilityOfCreatingASubscription(Long userSubscriberId, Long userPublisherId) {
        if (userSubscriberId.equals(userPublisherId))
            throw new AccessDeniedException("Impossible to subscribe to yourself");

        if (subscriptionRepository.existsByUserSubscriberIdAndUserPublisherId(userSubscriberId, userPublisherId))
            throw new EntityExistsException("This subscription already exists");

        if (!userInfoGetter.existsUserById(userPublisherId))
            throw new EntityNotFoundException("Publisher with id=" + userPublisherId + " not found");
    }

    private Subscription getSubscriptionById(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription with id=" + subscriptionId + " not found"));
    }
}