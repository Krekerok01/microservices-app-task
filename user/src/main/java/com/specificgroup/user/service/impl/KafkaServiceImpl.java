package com.specificgroup.user.service.impl;

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
    private final KafkaTemplate<String, Long> kafka;

    @Override
    public void notify(String topicName, Long userId) {
        kafka.send(topicName, userId);
        log.info("User {} requested to delete all his posts. Sent successfully in the topic {}", userId, topicName);
    }
}
