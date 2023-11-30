package com.specificgroup.user.service;

/**
 * Provides methods for sending data to the Kafka.
 */
public interface KafkaService {
    void notify(String topicName, Long userId);
}
