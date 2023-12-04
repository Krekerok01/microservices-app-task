package com.specificgroup.notification.service;

import com.specificgroup.notification.dto.MessageDto;

import javax.mail.MessagingException;

public interface MailService {
    void sendMessage(MessageDto message) throws MessagingException;
}
