package com.specificgroup.subscription.service;

import com.specificgroup.subscription.dto.UserServiceMessage;
import com.specificgroup.subscription.entity.Subscription;

import java.util.List;

/**
 * Provides methods for processing the input data from the controller and sending it to the repository
 */
public interface SubscriptionService {
    /**
     * Create a subscription between users
     *
     * @param userSubscriberId an id of the user who wants to subscribe
     * @param userPublisherId an id of the user being subscribed to
     * @return an id(long) of the record in the database
     */
    Long createSubscription(Long userSubscriberId, Long userPublisherId);

    /**
     * Get all subscriptions from the database
     *
     * @return a list subscriptions
     */
    List<Subscription> getAllSubscriptions();

    /**
     * Get all subscriptions for a specific user from the database
     *
     * @return a list of subscriptions IDs
     */
    List<Long> getSubscriptionsBySubscriberId(Long userSubscriberId);

    /**
     * Delete a subscription between users
     *
     * @param userPublisherId an id of the user being unsubscribed to
     * @param userSubscriberId an id of the user who wants to unsubscribe
     */
    void deleteSubscription(Long userPublisherId, Long userSubscriberId);

    /**
     * Delete all subscriptions for a specific user
     *
     * @param message a request from kafka the user service
     */
    void deleteSubscriptionsByUserId(UserServiceMessage message);
}