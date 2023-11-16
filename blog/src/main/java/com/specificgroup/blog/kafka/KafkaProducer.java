package com.specificgroup.blog.kafka;

import com.specificgroup.blog.dto.kafka.BlogServiceResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@EnableKafka
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafka;

    public void notify(String topicName, BlogServiceResponseMessage message) {
        kafka.send(topicName, message);
        log.info("Message {} has been successfully sent to the {} topic", message, topicName);
    }
}
