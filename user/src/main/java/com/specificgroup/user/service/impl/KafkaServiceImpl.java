package com.specificgroup.user.service.impl;

import com.specificgroup.user.model.dto.BlogServiceMessage;
import com.specificgroup.user.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@EnableKafka
@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {
    private final KafkaTemplate<String, Object> kafka;

    @Override
    public void notify(String topicName, Long userId) {
        BlogServiceMessage message = BlogServiceMessage.builder().userId(userId).build();
        kafka.send(topicName, message);
        log.info("User {} requested to delete all his posts. Sent successfully to the topic {}", userId, topicName);
    }
}
