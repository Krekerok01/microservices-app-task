package com.specificgroup.user.service;

public interface KafkaService {
    void notify(String topicName, Long userId);
}
