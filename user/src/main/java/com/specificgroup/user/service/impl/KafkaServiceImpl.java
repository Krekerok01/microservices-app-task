package com.specificgroup.user.service.impl;

import com.specificgroup.user.model.dto.BlogServiceMessage;
import com.specificgroup.user.model.dto.message.MailMessageDto;
import com.specificgroup.user.model.dto.message.MessageContent;
import com.specificgroup.user.model.dto.message.MessageType;
import com.specificgroup.user.service.KafkaService;
import com.specificgroup.user.util.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@EnableKafka
@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {
    private final KafkaTemplate<String, Object> kafka;
    private final Logger logger;

    @Override
    public void notify(String topicName, Long userId) {
        BlogServiceMessage message = BlogServiceMessage.builder().userId(userId).build();
        kafka.send(topicName, message);
        logger.info("Request to delete User " + userId +
                " information has been successfully sent to the topic " + topicName);
    }

    @Override
    public void notify(String topicName, String username, String destination, MessageType type) {
        MessageContent messageContent = switch (type) {
            case REGISTRATION -> new MessageContent(
                    String.format("Welcome, %s!", username),
                    "Thank you for registering in our service!"
            );
            case PASSWORD_CHANGE -> new MessageContent(
                    "Successful password change!",
                    String.format("Dear %s, your password was changed!", username)
            );
        };
        MailMessageDto message = new MailMessageDto(
                destination,
                type,
                messageContent
        );
        kafka.send(topicName, message);
    }
}
