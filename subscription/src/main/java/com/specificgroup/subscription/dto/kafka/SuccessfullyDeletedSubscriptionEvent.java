package com.specificgroup.subscription.dto.kafka;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SuccessfullyDeletedSubscriptionEvent {
    Long deletedUserId;
    String message;
}