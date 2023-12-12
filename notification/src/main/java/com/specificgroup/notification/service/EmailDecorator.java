package com.specificgroup.notification.service;

import com.specificgroup.notification.dto.NotifyEvent;

/**
 * Provides method to fill the message with content.
 */
public interface EmailDecorator {
    String modifyEmailContent(NotifyEvent message);
}
