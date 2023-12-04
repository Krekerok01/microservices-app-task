package com.specificgroup.user.service;

import com.specificgroup.user.model.dto.message.MessageType;

/**
 * Provides methods for sending data to the Kafka.
 */
public interface KafkaService {
    void notify(String topicName, Long userId);

    void notify(String topicName, String username, String destination, MessageType type);
}
