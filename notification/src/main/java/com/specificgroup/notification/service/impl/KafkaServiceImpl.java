package com.specificgroup.notification.service.impl;

import com.specificgroup.notification.dto.MessageDto;
import com.specificgroup.notification.exception.MailSendingException;
import com.specificgroup.notification.service.KafkaService;
import com.specificgroup.notification.service.MailService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaServiceImpl implements KafkaService {

    private final MailService mailService;

    @Autowired
    public KafkaServiceImpl(MailService mailService) {
        this.mailService = mailService;
    }

    @KafkaListener(topics = "${spring.kafka.topics.notifications.registry}")
    @Override
    public void consumeUserRegistration(MessageDto message) {
        try {
            mailService.sendMessage(message);
        } catch (javax.mail.MessagingException e) {
            throw new MailSendingException(e.getMessage());
        }
        log.info("User {} was successfully registered", message.getDestinationEmail());
    }

    @KafkaListener(topics = "${spring.kafka.topics.notifications.password_change}")
    @Override
    public void consumePasswordChanging(MessageDto message) {
        try {
            mailService.sendMessage(message);
        } catch (javax.mail.MessagingException e) {
            throw new MailSendingException(e.getMessage());
        }
        log.info("User {} changed password successfully", message.getDestinationEmail());
    }
}
