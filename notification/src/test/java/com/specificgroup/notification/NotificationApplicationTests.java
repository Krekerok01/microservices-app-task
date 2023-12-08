package com.specificgroup.notification;

import com.specificgroup.notification.dto.MessageType;
import com.specificgroup.notification.dto.NotifyEvent;
import com.specificgroup.notification.service.MailService;
import com.specificgroup.notification.service.impl.KafkaServiceImpl;
import com.specificgroup.notification.util.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@ExtendWith(MockitoExtension.class)
@DisplayName("Notification service test")
class NotificationApplicationTests {
    @Mock
    private MailService mailService;
    @Mock
    private Logger logger;

    private static NotifyEvent notifyEvent;

    @BeforeAll
    static void init() {
        notifyEvent = new NotifyEvent(
                "test",
                MessageType.REGISTRATION,
                "test"
        );
    }

    @Test
    @DisplayName("User registration message test")
    void userRegistrationMessageTest() {
        KafkaServiceImpl kafkaService = new KafkaServiceImpl(mailService, logger);
        kafkaService.consumeUserRegistration(notifyEvent);

        Assertions.assertDoesNotThrow(() -> Mockito.verify(
                        mailService,
                        Mockito.times(1))
                .sendMessage(any(NotifyEvent.class)));
        verifyNoMoreInteractions(mailService);
    }

    @Test
    @DisplayName("Password changing message test")
    void userPasswordChangingMessageTest() {
        KafkaServiceImpl kafkaService = new KafkaServiceImpl(mailService, logger);
        kafkaService.consumePasswordChanging(notifyEvent);

        Assertions.assertDoesNotThrow(() -> Mockito.verify(
                        mailService,
                        Mockito.times(1))
                .sendMessage(any(NotifyEvent.class)));
        verifyNoMoreInteractions(mailService);
    }
}