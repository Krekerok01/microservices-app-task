package com.specificgroup.subscription.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SubscriptionServiceResponseMessage {
    Long deletedUserId;
    String message;
}