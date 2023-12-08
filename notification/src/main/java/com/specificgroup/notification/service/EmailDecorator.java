package com.specificgroup.notification.service;

import com.specificgroup.notification.dto.NotifyEvent;
import org.springframework.stereotype.Service;

@Service
public interface EmailDecorator {
    String modifyEmailContent(NotifyEvent message);
}
