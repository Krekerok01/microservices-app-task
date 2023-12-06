package com.specificgroup.blog.kafka;

import com.specificgroup.blog.dto.kafka.BlogServiceResponseMessage;
import com.specificgroup.blog.util.logger.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Provides methods for sending data to the Kafka.
 */
@EnableKafka
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafka;
    private final Logger logger;

    public void notify(String topicName, BlogServiceResponseMessage message) {
        kafka.send(topicName, message);
        logger.info("Message " + message + " has been successfully sent to the " +
                topicName + " topic");
    }
}
