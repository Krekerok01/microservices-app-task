package com.specificgroup.subscription.service.impl;

import com.specificgroup.subscription.entity.Subscription;
import com.specificgroup.subscription.repository.SubscriptionRepository;
import com.specificgroup.subscription.service.SubscriptionService;
import com.specificgroup.subscription.util.getter.UserInfoGetter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService{

    private final SubscriptionRepository subscriptionRepository;
    private final UserInfoGetter userInfoGetter;

    @Override
    @Transactional
    public Long createSubscription(Long userSubscriberId, Long userPublisherId) {
        log.info("User with id={} subscribing to user with id={}", userSubscriberId, userPublisherId);
        if (!userInfoGetter.existsUserById(userPublisherId))
            throw new RuntimeException("User publisher not found");

        Subscription subscription = Subscription.builder()
                .userSubscriberId(userSubscriberId)
                .userPublisherId(userPublisherId)
                .build();
        return subscriptionRepository.save(subscription)
                .getSubscriptionId();
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        log.info("Getting all subscriptions");
        return subscriptionRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteSubscription(Long subscriptionId, Long userSubscriberId) {
        log.info("Deleting subscription with id={}", subscriptionId);

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                        .orElseThrow(() -> new RuntimeException("Message"));
        if (!subscription.getUserSubscriberId().equals(userSubscriberId))
            throw new RuntimeException("Forbidden");

        subscriptionRepository.delete(subscription);
    }
}
