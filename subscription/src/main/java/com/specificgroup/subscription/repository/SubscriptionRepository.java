package com.specificgroup.subscription.repository;

import com.specificgroup.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByUserSubscriberId(Long userSubscriberId);

    boolean existsByUserSubscriberIdAndUserPublisherId(Long userSubscriberId, Long userPublisherId);
}
