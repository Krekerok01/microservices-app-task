package com.specificgroup.subscription.service;

import com.specificgroup.subscription.dto.UserServiceMessage;
import com.specificgroup.subscription.entity.Subscription;

import java.util.List;

public interface SubscriptionService {
    Long createSubscription(Long userSubscriberId, Long userPublisherId);
    List<Subscription> getAllSubscriptions();
    List<Long> getSubscriptionsBySubscriberId(Long userSubscriberId);
    void deleteSubscription(Long subscriptionId, Long userSubscriberId);
    void deleteSubscriptionsByUserId(UserServiceMessage message);
}