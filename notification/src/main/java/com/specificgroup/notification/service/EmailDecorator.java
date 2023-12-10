package com.specificgroup.notification.service;

import com.specificgroup.notification.dto.NotifyEvent;
import org.springframework.stereotype.Service;

/**
 * Provides method to fill the message with content.
 */
@Service
public interface EmailDecorator {
    String modifyEmailContent(NotifyEvent message);
}