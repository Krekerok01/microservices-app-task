package com.specificgroup.notification.service.impl;


import com.specificgroup.notification.dto.NotifyEvent;
import com.specificgroup.notification.service.EmailDecorator;
import com.specificgroup.notification.service.MailService;
import com.specificgroup.notification.util.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final EmailDecorator emailDecorator;
    private final Logger logger;

    @Override
    public void sendMessage(NotifyEvent message) throws MessagingException {
        var mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo("vladislavsavko2003@gmail.com");
        helper.setSubject(message.getMessageType().getName());
        helper.setText(emailDecorator.modifyEmailContent(message), true);

        javaMailSender.send(mimeMessage);

        logger.info("Message: " + message + " successfully sent to " + "...email...");
    }
}