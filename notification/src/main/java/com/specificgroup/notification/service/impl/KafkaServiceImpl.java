package com.specificgroup.notification.service.impl;

import com.specificgroup.notification.dto.NotifyEvent;
import com.specificgroup.notification.exception.MailSendingException;
import com.specificgroup.notification.service.KafkaService;
import com.specificgroup.notification.service.MailService;
import com.specificgroup.notification.util.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    private final MailService mailService;
    private final Logger logger;

    @KafkaListener(topics = "${spring.kafka.topics.notifications.registry}")
    @Override
    public void consumeUserRegistration(NotifyEvent event) {
        try {
            mailService.sendMessage(event);
        } catch (javax.mail.MessagingException e) {
            logger.error("Message sending error: " + e.getMessage());
            throw new MailSendingException(e.getMessage());
        }
        logger.info("User " + event.getDestinationEmail() + " was successfully registered");
    }

    @KafkaListener(topics = "${spring.kafka.topics.notifications.password_change}")
    @Override
    public void consumePasswordChanging(NotifyEvent message) {
        try {
            mailService.sendMessage(message);
        } catch (javax.mail.MessagingException e) {
            logger.error("Message sending error: " + e.getMessage());
            throw new MailSendingException(e.getMessage());
        }
        logger.info(String.format("User %s has changed password successfully", message.getDestinationEmail()));
    }

    @KafkaListener(topics = "${spring.kafka.topics.notifications.email_change}")
    @Override
    public void consumeEmailChanging(NotifyEvent message) {
        try {
            mailService.sendMessage(message);
        } catch (javax.mail.MessagingException e) {
            logger.error("Message sending error: " + e.getMessage());
            throw new MailSendingException(e.getMessage());
        }
        logger.info(String.format("User %s has changed password successfully", message.getDestinationEmail()));
    }
}