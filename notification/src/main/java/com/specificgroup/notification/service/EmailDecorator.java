package com.specificgroup.notification.service;

import com.specificgroup.notification.dto.MessageDto;
import org.springframework.stereotype.Service;

@Service
public interface EmailDecorator {
    String modifyEmailContent(MessageDto message);
}
