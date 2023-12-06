package com.specificgroup.notification;

import com.specificgroup.notification.dto.MessageDto;
import com.specificgroup.notification.dto.MessageType;
import com.specificgroup.notification.dto.message.MessageContent;
import com.specificgroup.notification.service.MailService;
import com.specificgroup.notification.service.impl.KafkaServiceImpl;
import com.specificgroup.notification.service.impl.MailServiceImpl;
import com.specificgroup.notification.util.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.verifyNoMoreInteractions;


@ExtendWith(MockitoExtension.class)
@DisplayName("Notification service test")
class NotificationApplicationTests {
    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MailService mailService;

    @Mock
    private Logger logger;

    private static MessageDto messageDto;

    @BeforeAll
    static void init() {
        messageDto = new MessageDto(
                "test",
                MessageType.REGISTRATION,
                new MessageContent(
                        "test", "test")
        );
    }


    @Test
    @DisplayName("Mail service test")
    void mailServiceTest() {
        MailServiceImpl mailService = new MailServiceImpl(javaMailSender, logger);
        mailService.sendMessage(messageDto);

        Mockito.verify(javaMailSender, Mockito.times(1)).send(Mockito.any(SimpleMailMessage.class));
        verifyNoMoreInteractions(javaMailSender);
    }

    @Test
    @DisplayName("User registration message test")
    void userRegistrationMessageTest() {
        KafkaServiceImpl kafkaService = new KafkaServiceImpl(mailService, logger);
        kafkaService.consumeUserRegistration(messageDto);

        Assertions.assertDoesNotThrow(() -> Mockito.verify(
                        mailService,
                        Mockito.times(1))
                .sendMessage(Mockito.any(MessageDto.class)));
        verifyNoMoreInteractions(mailService);
    }

    @Test
    @DisplayName("Password changing message test")
    void userPasswordChangingMessageTest() {
        KafkaServiceImpl kafkaService = new KafkaServiceImpl(mailService, logger);
        kafkaService.consumePasswordChanging(messageDto);

        Assertions.assertDoesNotThrow(() -> Mockito.verify(
                        mailService,
                        Mockito.times(1))
                .sendMessage(Mockito.any(MessageDto.class)));
        verifyNoMoreInteractions(mailService);
    }
}