package com.specificgroup.notification.service.impl;


import com.specificgroup.notification.dto.MessageDto;
import com.specificgroup.notification.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMessage(MessageDto message) {
        SimpleMailMessage m = new SimpleMailMessage();
        m.setTo("vladislavsavko2003@gmail.com");
        m.setSubject(message.getContent().getHeader());
        m.setText(message.getContent().getText());

        javaMailSender.send(m);
    }
}