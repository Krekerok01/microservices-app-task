package com.specificgroup.notification.service.impl;


import com.specificgroup.notification.dto.MessageDto;
import com.specificgroup.notification.service.MailService;
import com.specificgroup.notification.util.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final Logger logger;

    @Override
    public void sendMessage(MessageDto message) {
        SimpleMailMessage m = new SimpleMailMessage();
        m.setTo("vladislavsavko2003@gmail.com");
        m.setSubject(message.getContent().getHeader());
        m.setText(message.getContent().getText());

        javaMailSender.send(m);

        logger.info("Message: " + message + " successfully sent to " + "...email...");
    }
}