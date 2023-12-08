package com.specificgroup.user.service.impl;

import com.specificgroup.user.model.dto.BlogServiceMessage;
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
        BlogServiceMessage message = BlogServiceMessage.builder().userId(userId).build();
        kafka.send(topicName, message);
        logger.info("Request to delete User " + userId +
                " information has been successfully sent to the topic " + topicName);
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
