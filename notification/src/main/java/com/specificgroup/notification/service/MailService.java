package com.specificgroup.notification.service;

import com.specificgroup.notification.dto.NotifyEvent;

import javax.mail.MessagingException;

/**
 * {@inheritDoc}
 */
public interface MailService {
    void sendMessage(NotifyEvent message) throws MessagingException;
}