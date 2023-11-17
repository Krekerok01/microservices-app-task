package com.specificgroup.subscription.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "subscriptions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "'subscriptionsSeqGenerator")
    @SequenceGenerator(name = "'subscriptionsSeqGenerator", sequenceName = "subscriptions_seq", allocationSize = 1)
    @Column(name = "subscription_id")
    private Long subscriptionId;

    @Column(name = "user_subscriber_id", nullable = false)
    private Long userSubscriberId;

    @Column(name = "user_publisher_id", nullable = false)
    private Long userPublisherId;
}