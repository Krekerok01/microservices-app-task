package com.specificgroup.subscription.service;

import com.specificgroup.subscription.entity.Subscription;

import java.util.List;

public interface SubscriptionService {
    Long createSubscription(Long userSubscriberId, Long userPublisherId);
    List<Subscription> getAllSubscriptions();
    List<Subscription> getSubscriptionsBySubscriberId(Long userSubscriberId);
    void deleteSubscription(Long subscriptionId, Long userSubscriberId);
}
