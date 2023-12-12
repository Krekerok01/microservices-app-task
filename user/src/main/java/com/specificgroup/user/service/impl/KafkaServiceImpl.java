package com.specificgroup.user.service.impl;

import com.specificgroup.user.model.dto.kafka.UserDeletedEvent;
import com.specificgroup.user.model.dto.message.MailMessageDto;
import com.specificgroup.user.model.dto.message.MessageType;
import com.specificgroup.user.service.KafkaService;
import com.specificgroup.user.util.Logger;
import lombok.RequiredArgsConstructor;
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
        UserDeletedEvent event = UserDeletedEvent.builder().userId(userId).build();
        kafka.send(topicName, event);
        logger.info(String.format("Request to delete User %d has been successfully sent to the topic %s", userId, topicName));
    }

    @Override
    public void notify(String topicName, String username, String destination, MessageType type) {
        kafka.send(topicName,
                new MailMessageDto(
                        destination,
                        type,
                        username
                ));
    }
}
