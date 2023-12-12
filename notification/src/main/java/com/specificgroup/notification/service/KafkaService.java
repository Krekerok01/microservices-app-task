package com.specificgroup.notification.service;

import com.specificgroup.notification.dto.NotifyEvent;

/**
 * Provides methods for sending data to the Kafka.
 */
public interface KafkaService {
    void consumeUserRegistration(NotifyEvent message);
    void consumePasswordChanging(NotifyEvent message);
    void consumeEmailChanging(NotifyEvent message);
}