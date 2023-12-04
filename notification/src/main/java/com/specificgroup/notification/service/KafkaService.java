package com.specificgroup.notification.service;

import com.specificgroup.notification.dto.MessageDto;

public interface KafkaService {
    void consumeUserRegistration(MessageDto message);
    void consumePasswordChanging(MessageDto message);
}
