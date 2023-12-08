package com.specificgroup.notification.service;

import com.specificgroup.notification.dto.NotifyEvent;

public interface KafkaService {
    void consumeUserRegistration(NotifyEvent message);
    void consumePasswordChanging(NotifyEvent message);
}
